package ui.widgets.doctor.view

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
import data.model.Doctor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.doctor.viewModel.DeleteDoctorState
import ui.widgets.doctor.viewModel.DeleteDoctorViewModel
import ui.widgets.doctor.viewModel.DoctorViewModel
import ui.widgets.doctor.viewModel.DoctorsListState
import ui.widgets.util.Center
import ui.widgets.util.showSuccess
import util.Action

@Composable
fun DoctorScreen() {
    val viewModel = remember { DoctorViewModel() }
    val deleteDoctorViewModel = remember { DeleteDoctorViewModel() }

    val doctorsListState = viewModel.doctorsListState.collectAsState().value
    val deleteDoctorState = deleteDoctorViewModel.deleteDoctorState.collectAsState().value

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navigator = LocalNavigator.current

    LaunchedEffect(doctorsListState) {
        if (doctorsListState == DoctorsListState.Initial)
            getDoctors(scope, viewModel)
    }

    LaunchedEffect(deleteDoctorState) {
        if (deleteDoctorState == DeleteDoctorState.Success) {
            getDoctors(scope, viewModel)
            showSuccess(scaffoldState, "Doctor", Action.Delete)
        }
        if (deleteDoctorState is DeleteDoctorState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(deleteDoctorState.error)
        }
    }

    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar(
            title = { Text("Doctor Screen") },
            actions = {
                IconButton(onClick = { getDoctors(scope, viewModel) }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh Doctors")
                }
            },
        )
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                navigator.invoke { EditDoctorScreen() }
            },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Insert Item")
        }
    }, modifier = Modifier
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            when (deleteDoctorState) {
                DeleteDoctorState.Loading -> LinearProgressIndicator()
                is DeleteDoctorState.Error -> Text(deleteDoctorState.error, color = Color.Red)
                else -> Unit
            }
            when (doctorsListState) {
                DoctorsListState.Loading -> Center {
                    CircularProgressIndicator()
                }

                is DoctorsListState.Success -> DoctorsList(doctorsListState, scaffoldState, deleteDoctorViewModel)
                is DoctorsListState.Error -> Text(doctorsListState.error, color = Color.Red)
                else -> Unit
            }
        }
    }
}

@Composable
private fun DoctorsList(
    doctorsListState: DoctorsListState.Success,
    scaffoldState: ScaffoldState,
    deleteDoctorViewModel: DeleteDoctorViewModel
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                DoctorTitleRow()
            }
            items(doctorsListState.doctors) { doctor ->
                DoctorRow(
                    doctor,
                    onDelete = {
                        scope.launch {
                            askDeletionPermission(scope, scaffoldState, deleteDoctorViewModel, it)
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
    deleteDoctorViewModel: DeleteDoctorViewModel,
    doctor: Doctor
) {
    val result = scaffoldState.snackbarHostState.showSnackbar(
        "Are you sure you want to delete this doctor?",
        actionLabel = "Yes"
    )
    if (result == SnackbarResult.ActionPerformed)
        deleteDoctor(scope, deleteDoctorViewModel, doctor)
}

private fun getDoctors(scope: CoroutineScope, viewModel: DoctorViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getDoctors() }
}

private fun deleteDoctor(scope: CoroutineScope, viewModel: DeleteDoctorViewModel, doctor: Doctor) {
    scope.launch(Dispatchers.IO) { viewModel.deleteDoctor(doctor) }
}
