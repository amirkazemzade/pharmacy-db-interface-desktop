package ui.widgets.med.view

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
import data.model.Med
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.med.viewModel.DeleteMedState
import ui.widgets.med.viewModel.DeleteMedViewModel
import ui.widgets.med.viewModel.MedViewModel
import ui.widgets.med.viewModel.MedsListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun MedScreen() {
    val viewModel = remember { MedViewModel() }
    val deleteMedViewModel = remember { DeleteMedViewModel() }

    val medsListState = viewModel.medsListState.collectAsState().value
    val deleteMedState = deleteMedViewModel.deleteMedState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(medsListState) {
        if (medsListState == MedsListState.Initial)
            getMeds(scope, viewModel)
    }

    LaunchedEffect(deleteMedState) {
        if (deleteMedState == DeleteMedState.Success) {
            getMeds(scope, viewModel)
            showSuccess(scaffoldState, "Med", Action.Delete)
        }
        if (deleteMedState is DeleteMedState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deleteMedState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Med Screen") },
            actions = {
                IconButton(onClick = { getMeds(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Meds")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditMedScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deleteMedState) {
                DeleteMedState.Loading -> LinearProgressIndicator()
                is DeleteMedState.Error -> Text(deleteMedState.error, color = Color.Red)
                else -> Unit
            }
            when (medsListState) {
                MedsListState.Initial -> Unit
                MedsListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is MedsListState.Success -> MedsList(medsListState, scaffoldState, deleteMedViewModel)
                is MedsListState.Error -> Text(medsListState.error, color = Color.Red)
            }
        }
    }
}

@Composable
private fun MedsList(
    medsListState: MedsListState.Success,
    scaffoldState: ScaffoldState,
    deleteMedViewModel: DeleteMedViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                MedTitleRow()
            }
            items(medsListState.meds) { med ->
                MedRow(
                    med,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deleteMedViewModel, it)
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
    deleteMedViewModel: DeleteMedViewModel,
    med: Med
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this med?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deleteMed(scope, deleteMedViewModel, med)
}

private fun getMeds(scope: CoroutineScope, viewModel: MedViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getMeds() }
}

private fun deleteMed(scope: CoroutineScope, viewModel: DeleteMedViewModel, med: Med) {
    scope.launch(Dispatchers.IO) { viewModel.deleteMed(med) }
}
