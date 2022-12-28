package ui.widgets.category.view

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
import data.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.category.viewModel.CategoryViewModel
import ui.widgets.category.viewModel.CategorysListState
import ui.widgets.category.viewModel.DeleteCategoryState
import ui.widgets.category.viewModel.DeleteCategoryViewModel
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun CategoryScreen() {
    val viewModel = remember { CategoryViewModel() }
    val deleteCategoryViewModel = remember { DeleteCategoryViewModel() }

    val categoriesListState = viewModel.categorysListState.collectAsState().value
    val deleteCategoryState = deleteCategoryViewModel.deleteCategoryState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(categoriesListState) {
        if (categoriesListState == CategorysListState.Initial)
            getCategories(scope, viewModel)
    }

    LaunchedEffect(deleteCategoryState) {
        if (deleteCategoryState == DeleteCategoryState.Success) {
            getCategories(scope, viewModel)
            showSuccess(scaffoldState, "Category", Action.Delete)
        }
        if (deleteCategoryState is DeleteCategoryState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deleteCategoryState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Category Screen") },
            actions = {
                IconButton(onClick = { getCategories(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Categories")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditCategoryScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deleteCategoryState) {
                DeleteCategoryState.Loading -> LinearProgressIndicator()
                is DeleteCategoryState.Error -> Text(deleteCategoryState.error, color = Color.Red)
                else -> Unit
            }
            when (categoriesListState) {
                CategorysListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is CategorysListState.Success -> CategoriesList(
                    categoriesListState,
                    scaffoldState,
                    deleteCategoryViewModel
                )

                is CategorysListState.Error -> Text(categoriesListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun CategoriesList(
    categoriesListState: CategorysListState.Success,
    scaffoldState: ScaffoldState,
    deleteCategoryViewModel: DeleteCategoryViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                CategoryTitleRow()
            }
            items(categoriesListState.categorys) { category ->
                CategoryRow(
                    category,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deleteCategoryViewModel, it)
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
    deleteCategoryViewModel: DeleteCategoryViewModel,
    category: Category
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this category?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deleteCategory(scope, deleteCategoryViewModel, category)
}

private fun getCategories(scope: CoroutineScope, viewModel: CategoryViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getCategorys() }
}

private fun deleteCategory(scope: CoroutineScope, viewModel: DeleteCategoryViewModel, category: Category) {
    scope.launch(Dispatchers.IO) { viewModel.deleteCategory(category) }
}
