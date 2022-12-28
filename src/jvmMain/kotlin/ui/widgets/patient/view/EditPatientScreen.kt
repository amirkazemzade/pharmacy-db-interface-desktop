package ui.widgets.patient.view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.model.Patient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.patient.viewModel.EditPatientState
import ui.widgets.patient.viewModel.UpdateOrAddPatientViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditPatientScreen(patient: Patient? = null) {
    val viewModel = remember { UpdateOrAddPatientViewModel() }
    val editPatientState = viewModel.editPatientState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${patient?.id ?: ""}")) }
    val firstNameValue = remember { mutableStateOf(TextFieldValue(patient?.firstName ?: "")) }
    val lastNameValue = remember { mutableStateOf(TextFieldValue(patient?.lastName ?: "")) }
    val nationalNumberValue = remember { mutableStateOf(TextFieldValue(patient?.nationalNumber ?: "")) }
    val phoneValue = remember { mutableStateOf(TextFieldValue(patient?.phone ?: "")) }
    val birthDateValue = remember { mutableStateOf(TextFieldValue("${patient?.birthDate ?: ""}")) }
    val insuranceIdValue = remember { mutableStateOf(TextFieldValue("${patient?.insuranceId ?: ""}")) }

    val isUpdating = patient != null

    val onDone = {
        scope.launch {
            if (!isUpdating && idValue.value.text.isNotBlank() && !idValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, idText)
                return@launch
            }
            if (firstNameValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, firstNameText)
                return@launch
            }
            if (lastNameValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, lastNameText)
                return@launch
            }
            if (nationalNumberValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, nationalNumberText)
                return@launch
            }
            if (birthDateValue.value.text.isBlank() || !birthDateValue.value.text.isDate()) {
                showInvalidValueSnackBar(scaffoldState, birthDateText)
                return@launch
            }
            if (!insuranceIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, insuranceIdText)
                return@launch
            }
            lateinit var newPatient: Patient

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val firstName = firstNameValue.value.text
            val lastName = lastNameValue.value.text
            val nationalNumber = nationalNumberValue.value.text
            val phone = phoneValue.value.text
            val birthDate = birthDateValue.value.text.toDate()
            val insuranceId = insuranceIdValue.value.text.toInt()

            if (isUpdating) {
                newPatient = patient!!.copy(
                    firstName = firstName,
                    lastName = lastName,
                    nationalNumber = nationalNumber,
                    phone = phone,
                    birthDate = birthDate,
                    insuranceId = insuranceId,
                )
                updatePatient(scope, viewModel, newPatient)
            } else {
                newPatient = Patient(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    nationalNumber = nationalNumber,
                    phone = phone,
                    birthDate = birthDate,
                    insuranceId = insuranceId,
                )
                addPatient(scope, viewModel, newPatient)
            }
        }
    }

    LaunchedEffect(viewModel.editPatientState.collectAsState().value) {
        if (editPatientState == EditPatientState.Success) {
            showSuccess(scaffoldState, "Patient", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { PatientScreen() }
        } else if (editPatientState is EditPatientState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editPatientState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Patient" else "Add Patient"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { PatientScreen() }
                        },
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Navigate Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            onDone()
                        },
                    ) {
                        Icon(Icons.Filled.Done, contentDescription = "Done")
                    }
                },
            )
        },
    ) {
        when (editPatientState) {
            EditPatientState.Loading -> Center {
                CircularProgressIndicator()
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    OutlinedTextField(
                        value = idValue.value,
                        onValueChange = { idValue.value = it },
                        label = { Text(idText) },
                        enabled = !isUpdating,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = firstNameValue.value,
                        onValueChange = { firstNameValue.value = it },
                        label = { Text(firstNameText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = lastNameValue.value,
                        onValueChange = { lastNameValue.value = it },
                        label = { Text(lastNameText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = nationalNumberValue.value,
                        onValueChange = { nationalNumberValue.value = it },
                        label = { Text(nationalNumberText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = phoneValue.value,
                        onValueChange = { phoneValue.value = it },
                        label = { Text(phoneText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = birthDateValue.value,
                        onValueChange = { birthDateValue.value = it },
                        label = { Text(birthDateText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = insuranceIdValue.value,
                        onValueChange = { insuranceIdValue.value = it },
                        label = { Text(insuranceIdText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}


private fun addPatient(scope: CoroutineScope, viewModel: UpdateOrAddPatientViewModel, patient: Patient) {
    scope.launch(Dispatchers.IO) { viewModel.addPatient(patient) }
}

private fun updatePatient(scope: CoroutineScope, viewModel: UpdateOrAddPatientViewModel, patient: Patient) {
    scope.launch(Dispatchers.IO) { viewModel.updatePatient(patient) }
}