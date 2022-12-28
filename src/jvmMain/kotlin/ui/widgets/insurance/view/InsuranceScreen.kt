package ui.widgets.insurance.view

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
import data.model.Insurance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.insurance.viewModel.DeleteInsuranceState
import ui.widgets.insurance.viewModel.DeleteInsuranceViewModel
import ui.widgets.insurance.viewModel.InsuranceViewModel
import ui.widgets.insurance.viewModel.InsurancesListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun InsuranceScreen() {
    val viewModel = remember { InsuranceViewModel() }
    val deleteInsuranceViewModel = remember { DeleteInsuranceViewModel() }

    val categoriesListState = viewModel.insurancesListState.collectAsState().value
    val deleteInsuranceState = deleteInsuranceViewModel.deleteInsuranceState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(categoriesListState) {
        if (categoriesListState == InsurancesListState.Initial)
            getCategories(scope, viewModel)
    }

    LaunchedEffect(deleteInsuranceState) {
        if (deleteInsuranceState == DeleteInsuranceState.Success) {
            getCategories(scope, viewModel)
            showSuccess(scaffoldState, "Insurance", Action.Delete)
        }
        if (deleteInsuranceState is DeleteInsuranceState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deleteInsuranceState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Insurance Screen") },
            actions = {
                IconButton(onClick = { getCategories(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Categories")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditInsuranceScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deleteInsuranceState) {
                DeleteInsuranceState.Loading -> LinearProgressIndicator()
                is DeleteInsuranceState.Error -> Text(deleteInsuranceState.error, color = Color.Red)
                else -> Unit
            }
            when (categoriesListState) {
                InsurancesListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is InsurancesListState.Success -> CategoriesList(
                    categoriesListState,
                    scaffoldState,
                    deleteInsuranceViewModel
                )

                is InsurancesListState.Error -> Text(categoriesListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun CategoriesList(
    categoriesListState: InsurancesListState.Success,
    scaffoldState: ScaffoldState,
    deleteInsuranceViewModel: DeleteInsuranceViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                InsuranceTitleRow()
            }
            items(categoriesListState.insurances) { insurance ->
                InsuranceRow(
                    insurance,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deleteInsuranceViewModel, it)
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
    deleteInsuranceViewModel: DeleteInsuranceViewModel,
    insurance: Insurance
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this insurance?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deleteInsurance(scope, deleteInsuranceViewModel, insurance)
}

private fun getCategories(scope: CoroutineScope, viewModel: InsuranceViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getInsurances() }
}

private fun deleteInsurance(scope: CoroutineScope, viewModel: DeleteInsuranceViewModel, insurance: Insurance) {
    scope.launch(Dispatchers.IO) { viewModel.deleteInsurance(insurance) }
}
