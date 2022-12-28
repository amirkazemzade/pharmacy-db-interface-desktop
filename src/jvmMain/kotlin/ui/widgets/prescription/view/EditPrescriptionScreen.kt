package ui.widgets.prescription.view

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
import data.model.Prescription
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.prescription.viewModel.EditPrescriptionState
import ui.widgets.prescription.viewModel.UpdateOrAddPrescriptionViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditPrescriptionScreen(prescription: Prescription? = null) {
    val viewModel = remember { UpdateOrAddPrescriptionViewModel() }
    val editPrescriptionState = viewModel.editPrescriptionState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${prescription?.id ?: ""}")) }
    val dateValue = remember { mutableStateOf(TextFieldValue("${prescription?.date ?: ""}")) }
    val totalPriceValue = remember { mutableStateOf(TextFieldValue("${prescription?.totalPrice ?: "0"}")) }
    val isPaidValue = remember { mutableStateOf(TextFieldValue("${prescription?.isPaid?.toInt() ?: "0"}")) }
    val doctorIdValue = remember { mutableStateOf(TextFieldValue("${prescription?.doctorId ?: ""}")) }
    val patientIdValue = remember { mutableStateOf(TextFieldValue("${prescription?.patientId ?: ""}")) }

    val isUpdating = prescription != null

    val onDone = {
        scope.launch {
            if (!isUpdating && idValue.value.text.isNotBlank() && !idValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, idText)
                return@launch
            }
            if (dateValue.value.text.isBlank() || !dateValue.value.text.isDate()) {
                showInvalidValueSnackBar(scaffoldState, dateText)
                return@launch
            }
            if (!totalPriceValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, totalPriceText)
                return@launch
            }
            if (!isPaidValue.value.text.isIntBool()) {
                showInvalidValueSnackBar(scaffoldState, isPaidText)
                return@launch
            }
            if (!doctorIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, doctorIdText)
                return@launch
            }
            if (!patientIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, patientIdText)
                return@launch
            }
            lateinit var newPrescription: Prescription

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val date = dateValue.value.text.toDate()
            val totalPrice = totalPriceValue.value.text.toLong()
            val isPaid = isPaidValue.value.text.toInt().toBoolean()
            val doctorId = doctorIdValue.value.text.toInt()
            val patientId = patientIdValue.value.text.toInt()

            if (isUpdating) {
                newPrescription = prescription!!.copy(
                    date = date,
                    totalPrice = totalPrice,
                    isPaid = isPaid,
                    doctorId = doctorId,
                    patientId = patientId,
                )
                updatePrescription(scope, viewModel, newPrescription)
            } else {
                newPrescription = Prescription(
                    id = id,
                    date = date,
                    totalPrice = totalPrice,
                    isPaid = isPaid,
                    doctorId = doctorId,
                    patientId = patientId,
                )
                addPrescription(scope, viewModel, newPrescription)
            }
        }
    }

    LaunchedEffect(viewModel.editPrescriptionState.collectAsState().value) {
        if (editPrescriptionState == EditPrescriptionState.Success) {
            showSuccess(scaffoldState, "Prescription", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { PrescriptionScreen() }
        } else if (editPrescriptionState is EditPrescriptionState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editPrescriptionState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Prescription" else "Add Prescription"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { PrescriptionScreen() }
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
        when (editPrescriptionState) {
            EditPrescriptionState.Loading -> Center {
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
                        value = dateValue.value,
                        onValueChange = { dateValue.value = it },
                        label = { Text(dateText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = totalPriceValue.value,
                        onValueChange = { totalPriceValue.value = it },
                        label = { Text(totalPriceText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = isPaidValue.value,
                        onValueChange = { isPaidValue.value = it },
                        label = { Text(isPaidText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = doctorIdValue.value,
                        onValueChange = { doctorIdValue.value = it },
                        label = { Text(doctorIdText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = patientIdValue.value,
                        onValueChange = { patientIdValue.value = it },
                        label = { Text(patientIdText) },
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


private fun addPrescription(
    scope: CoroutineScope,
    viewModel: UpdateOrAddPrescriptionViewModel,
    prescription: Prescription
) {
    scope.launch(Dispatchers.IO) { viewModel.addPrescription(prescription) }
}

private fun updatePrescription(
    scope: CoroutineScope,
    viewModel: UpdateOrAddPrescriptionViewModel,
    prescription: Prescription
) {
    scope.launch(Dispatchers.IO) { viewModel.updatePrescription(prescription) }
}