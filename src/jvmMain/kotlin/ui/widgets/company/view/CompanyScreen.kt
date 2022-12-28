package ui.widgets.company.view

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import data.model.Company
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.company.viewModel.CompaniesListState
import ui.widgets.company.viewModel.CompanyViewModel
import ui.widgets.company.viewModel.DeleteCompanyState
import ui.widgets.company.viewModel.DeleteCompanyViewModel
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun CompanyScreen() {
    val viewModel = remember { CompanyViewModel() }
    val deleteCompanyViewModel = remember { DeleteCompanyViewModel() }

    val categoriesListState = viewModel.companiesListState.collectAsState().value
    val deleteCompanyState = deleteCompanyViewModel.deleteCompanyState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(categoriesListState) {
        if (categoriesListState == CompaniesListState.Initial)
            getCategories(scope, viewModel)
    }

    LaunchedEffect(deleteCompanyState) {
        if (deleteCompanyState == DeleteCompanyState.Success) {
            getCategories(scope, viewModel)
            showSuccess(scaffoldState, "Company", Action.Delete)
        }
        if (deleteCompanyState is DeleteCompanyState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deleteCompanyState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Company Screen") },
            actions = {
                IconButton(onClick = { getCategories(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Categories")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditCompanyScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deleteCompanyState) {
                DeleteCompanyState.Loading -> LinearProgressIndicator()
                is DeleteCompanyState.Error -> Text(deleteCompanyState.error, color = Color.Red)
                else -> Unit
            }
            when (categoriesListState) {
                CompaniesListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is CompaniesListState.Success -> CategoriesList(
                    categoriesListState,
                    scaffoldState,
                    deleteCompanyViewModel
                )

                is CompaniesListState.Error -> Text(categoriesListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun CategoriesList(
    categoriesListState: CompaniesListState.Success,
    scaffoldState: ScaffoldState,
    deleteCompanyViewModel: DeleteCompanyViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                CompanyTitleRow()
            }
            items(categoriesListState.companies) { company ->
                CompanyRow(
                    company,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deleteCompanyViewModel, it)
                        }
                    },
                )
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(), adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}

suspend fun askDeletionPermission(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    deleteCompanyViewModel: DeleteCompanyViewModel,
    company: Company
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this company?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deleteCompany(scope, deleteCompanyViewModel, company)
}

private fun getCategories(scope: CoroutineScope, viewModel: CompanyViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getCompanies() }
}

private fun deleteCompany(scope: CoroutineScope, viewModel: DeleteCompanyViewModel, company: Company) {
    scope.launch(Dispatchers.IO) { viewModel.deleteCompany(company) }
}
