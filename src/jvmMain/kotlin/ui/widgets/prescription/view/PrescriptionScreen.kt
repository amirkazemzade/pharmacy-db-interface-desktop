package ui.widgets.prescription.view

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
import data.model.Prescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.prescription.viewModel.DeletePrescriptionState
import ui.widgets.prescription.viewModel.DeletePrescriptionViewModel
import ui.widgets.prescription.viewModel.PrescriptionViewModel
import ui.widgets.prescription.viewModel.PrescriptionsListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun PrescriptionScreen() {
    val viewModel = remember { PrescriptionViewModel() }
    val deletePrescriptionViewModel = remember { DeletePrescriptionViewModel() }

    val prescriptionsListState = viewModel.prescriptionsListState.collectAsState().value
    val deletePrescriptionState = deletePrescriptionViewModel.deletePrescriptionState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(prescriptionsListState) {
        if (prescriptionsListState == PrescriptionsListState.Initial)
            getPrescriptions(scope, viewModel)
    }

    LaunchedEffect(deletePrescriptionState) {
        if (deletePrescriptionState == DeletePrescriptionState.Success) {
            getPrescriptions(scope, viewModel)
            showSuccess(scaffoldState, "Prescription", Action.Delete)
        }
        if (deletePrescriptionState is DeletePrescriptionState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deletePrescriptionState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Prescription Screen") },
            actions = {
                IconButton(onClick = { getPrescriptions(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Prescriptions")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditPrescriptionScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deletePrescriptionState) {
                DeletePrescriptionState.Loading -> LinearProgressIndicator()
                is DeletePrescriptionState.Error -> Text(deletePrescriptionState.error, color = Color.Red)
                else -> Unit
            }
            when (prescriptionsListState) {
                PrescriptionsListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is PrescriptionsListState.Success -> PrescriptionsList(
                    prescriptionsListState,
                    scaffoldState,
                    deletePrescriptionViewModel
                )

                is PrescriptionsListState.Error -> Text(prescriptionsListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun PrescriptionsList(
    prescriptionsListState: PrescriptionsListState.Success,
    scaffoldState: ScaffoldState,
    deletePrescriptionViewModel: DeletePrescriptionViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                PrescriptionTitleRow()
            }
            items(prescriptionsListState.prescriptions) { prescription ->
                PrescriptionRow(
                    prescription,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deletePrescriptionViewModel, it)
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
    deletePrescriptionViewModel: DeletePrescriptionViewModel,
    prescription: Prescription
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this prescription?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deletePrescription(scope, deletePrescriptionViewModel, prescription)
}

private fun getPrescriptions(scope: CoroutineScope, viewModel: PrescriptionViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getPrescriptions() }
}

private fun deletePrescription(
    scope: CoroutineScope,
    viewModel: DeletePrescriptionViewModel,
    prescription: Prescription
) {
    scope.launch(Dispatchers.IO) { viewModel.deletePrescription(prescription) }
}
