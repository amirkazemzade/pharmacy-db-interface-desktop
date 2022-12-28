package ui.widgets.patient.view

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
import data.model.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.patient.viewModel.DeletePatientState
import ui.widgets.patient.viewModel.DeletePatientViewModel
import ui.widgets.patient.viewModel.PatientViewModel
import ui.widgets.patient.viewModel.PatientsListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun PatientScreen() {
    val viewModel = remember { PatientViewModel() }
    val deletePatientViewModel = remember { DeletePatientViewModel() }

    val patientsListState = viewModel.patientsListState.collectAsState().value
    val deletePatientState = deletePatientViewModel.deletePatientState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(patientsListState) {
        if (patientsListState == PatientsListState.Initial)
            getPatients(scope, viewModel)
    }

    LaunchedEffect(deletePatientState) {
        if (deletePatientState == DeletePatientState.Success) {
            getPatients(scope, viewModel)
            showSuccess(scaffoldState, "Patient", Action.Delete)
        }
        if (deletePatientState is DeletePatientState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deletePatientState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Patient Screen") },
            actions = {
                IconButton(onClick = { getPatients(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Patients")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditPatientScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deletePatientState) {
                DeletePatientState.Loading -> LinearProgressIndicator()
                is DeletePatientState.Error -> Text(deletePatientState.error, color = Color.Red)
                else -> Unit
            }
            when (patientsListState) {
                PatientsListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is PatientsListState.Success -> PatientsList(patientsListState, scaffoldState, deletePatientViewModel)
                is PatientsListState.Error -> Text(patientsListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun PatientsList(
    patientsListState: PatientsListState.Success,
    scaffoldState: ScaffoldState,
    deletePatientViewModel: DeletePatientViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                PatientTitleRow()
            }
            items(patientsListState.patients) { patient ->
                PatientRow(
                    patient,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deletePatientViewModel, it)
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
    deletePatientViewModel: DeletePatientViewModel,
    patient: Patient
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this patient?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deletePatient(scope, deletePatientViewModel, patient)
}

private fun getPatients(scope: CoroutineScope, viewModel: PatientViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getPatients() }
}

private fun deletePatient(scope: CoroutineScope, viewModel: DeletePatientViewModel, patient: Patient) {
    scope.launch(Dispatchers.IO) { viewModel.deletePatient(patient) }
}
