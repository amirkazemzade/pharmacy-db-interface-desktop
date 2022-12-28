package ui.widgets.insurance.view

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
import data.model.Insurance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.insurance.viewModel.EditInsuranceState
import ui.widgets.insurance.viewModel.UpdateOrAddInsuranceViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditInsuranceScreen(insurance: Insurance? = null) {
    val viewModel = remember { UpdateOrAddInsuranceViewModel() }
    val editInsuranceState = viewModel.editInsuranceState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${insurance?.id ?: ""}")) }
    val nameValue = remember { mutableStateOf(TextFieldValue(insurance?.name ?: "")) }
    val emailValue = remember { mutableStateOf(TextFieldValue(insurance?.email ?: "")) }
    val addressValue = remember { mutableStateOf(TextFieldValue(insurance?.address ?: "")) }

    val isUpdating = insurance != null

    val onDone = {
        scope.launch {
            if (!isUpdating && idValue.value.text.isNotBlank() && !idValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, idText)
                return@launch
            }
            if (nameValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, nameText)
                return@launch
            }
            lateinit var newInsurance: Insurance

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val name = nameValue.value.text
            val email = emailValue.value.text
            val address = addressValue.value.text

            if (isUpdating) {
                newInsurance = insurance!!.copy(
                    name = name,
                    email = email,
                    address = address
                )
                updateInsurance(scope, viewModel, newInsurance)
            } else {
                newInsurance = Insurance(
                    id = id,
                    name = name,
                    email = email,
                    address = address
                )
                addInsurance(scope, viewModel, newInsurance)
            }
        }
    }

    LaunchedEffect(viewModel.editInsuranceState.collectAsState().value) {
        if (editInsuranceState == EditInsuranceState.Success) {
            showSuccess(scaffoldState, "Insurance", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { InsuranceScreen() }
        } else if (editInsuranceState is EditInsuranceState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editInsuranceState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Insurance" else "Add Insurance"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { InsuranceScreen() }
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
        when (editInsuranceState) {
            EditInsuranceState.Loading -> Center {
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
                        value = nameValue.value,
                        onValueChange = { nameValue.value = it },
                        label = { Text(nameText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
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
                        value = addressValue.value,
                        onValueChange = { addressValue.value = it },
                        label = { Text(addressText) },
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


private fun addInsurance(scope: CoroutineScope, viewModel: UpdateOrAddInsuranceViewModel, insurance: Insurance) {
    scope.launch(Dispatchers.IO) { viewModel.addInsurance(insurance) }
}

private fun updateInsurance(scope: CoroutineScope, viewModel: UpdateOrAddInsuranceViewModel, insurance: Insurance) {
    scope.launch(Dispatchers.IO) { viewModel.updateInsurance(insurance) }
}