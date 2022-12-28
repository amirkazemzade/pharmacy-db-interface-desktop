package ui.widgets.doctor.view

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
import data.model.Doctor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.doctor.viewModel.EditDoctorState
import ui.widgets.doctor.viewModel.UpdateOrAddDoctorViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditDoctorScreen(doctor: Doctor? = null) {
    val viewModel = remember { UpdateOrAddDoctorViewModel() }
    val editDoctorState = viewModel.editDoctorState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${doctor?.id ?: ""}")) }
    val firstNameValue = remember { mutableStateOf(TextFieldValue(doctor?.firstName ?: "")) }
    val lastNameValue = remember { mutableStateOf(TextFieldValue(doctor?.lastName ?: "")) }
    val emailValue = remember { mutableStateOf(TextFieldValue(doctor?.email ?: "")) }
    val phoneValue = remember { mutableStateOf(TextFieldValue(doctor?.phone ?: "")) }
    val addressValue = remember { mutableStateOf(TextFieldValue(doctor?.address ?: "")) }
    val licenceIdValue = remember { mutableStateOf(TextFieldValue(doctor?.licenceId ?: "")) }

    val isUpdating = doctor != null

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
            if (!licenceIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, licenceIdText)
                return@launch
            }
            lateinit var newDoctor: Doctor

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val firstName = firstNameValue.value.text
            val lastName = lastNameValue.value.text
            val email = emailValue.value.text
            val phone = phoneValue.value.text
            val address = addressValue.value.text
            val licenceId = licenceIdValue.value.text

            if (isUpdating) {
                newDoctor = doctor!!.copy(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    address = address,
                    licenceId = licenceId,
                )
                updateDoctor(scope, viewModel, newDoctor)
            } else {
                newDoctor = Doctor(
                    id = id,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    phone = phone,
                    address = address,
                    licenceId = licenceId,
                )
                addDoctor(scope, viewModel, newDoctor)
            }
        }
    }

    LaunchedEffect(viewModel.editDoctorState.collectAsState().value) {
        if (editDoctorState == EditDoctorState.Success) {
            showSuccess(scaffoldState, "Doctor", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { DoctorScreen() }
        } else if (editDoctorState is EditDoctorState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editDoctorState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Doctor" else "Add Doctor"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { DoctorScreen() }
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
        when (editDoctorState) {
            EditDoctorState.Loading -> Center {
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
                        value = emailValue.value,
                        onValueChange = { emailValue.value = it },
                        label = { Text(emailText) },
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
                        value = addressValue.value,
                        onValueChange = { addressValue.value = it },
                        label = { Text(addressText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = licenceIdValue.value,
                        onValueChange = { licenceIdValue.value = it },
                        label = { Text(licenceIdText) },
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


private fun addDoctor(scope: CoroutineScope, viewModel: UpdateOrAddDoctorViewModel, doctor: Doctor) {
    scope.launch(Dispatchers.IO) { viewModel.addDoctor(doctor) }
}

private fun updateDoctor(scope: CoroutineScope, viewModel: UpdateOrAddDoctorViewModel, doctor: Doctor) {
    scope.launch(Dispatchers.IO) { viewModel.updateDoctor(doctor) }
}