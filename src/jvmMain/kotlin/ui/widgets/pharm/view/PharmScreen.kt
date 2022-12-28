package ui.widgets.pharm.view

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
import data.model.Pharm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.pharm.viewModel.DeletePharmState
import ui.widgets.pharm.viewModel.DeletePharmViewModel
import ui.widgets.pharm.viewModel.PharmViewModel
import ui.widgets.pharm.viewModel.PharmsListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun PharmScreen() {
    val viewModel = remember { PharmViewModel() }
    val deletePharmViewModel = remember { DeletePharmViewModel() }

    val pharmsListState = viewModel.pharmsListState.collectAsState().value
    val deletePharmState = deletePharmViewModel.deletePharmState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(pharmsListState) {
        if (pharmsListState == PharmsListState.Initial)
            getPharms(scope, viewModel)
    }

    LaunchedEffect(deletePharmState) {
        if (deletePharmState == DeletePharmState.Success) {
            getPharms(scope, viewModel)
            showSuccess(scaffoldState, "Pharm", Action.Delete)
        }
        if (deletePharmState is DeletePharmState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deletePharmState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Pharm Screen") },
            actions = {
                IconButton(onClick = { getPharms(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Pharms")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditPharmScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deletePharmState) {
                DeletePharmState.Loading -> LinearProgressIndicator()
                is DeletePharmState.Error -> Text(deletePharmState.error, color = Color.Red)
                else -> Unit
            }
            when (pharmsListState) {
                PharmsListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is PharmsListState.Success -> PharmsList(pharmsListState, scaffoldState, deletePharmViewModel)
                is PharmsListState.Error -> Text(pharmsListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun PharmsList(
    pharmsListState: PharmsListState.Success,
    scaffoldState: ScaffoldState,
    deletePharmViewModel: DeletePharmViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                PharmTitleRow()
            }
            items(pharmsListState.pharms) { pharm ->
                PharmRow(
                    pharm,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deletePharmViewModel, it)
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
    deletePharmViewModel: DeletePharmViewModel,
    pharm: Pharm
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this pharm?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deletePharm(scope, deletePharmViewModel, pharm)
}

private fun getPharms(scope: CoroutineScope, viewModel: PharmViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getPharms() }
}

private fun deletePharm(scope: CoroutineScope, viewModel: DeletePharmViewModel, pharm: Pharm) {
    scope.launch(Dispatchers.IO) { viewModel.deletePharm(pharm) }
}
