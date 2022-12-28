package ui.widgets.company.view

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
import data.model.Company
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.company.viewModel.EditCompanyState
import ui.widgets.company.viewModel.UpdateOrAddCompanyViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditCompanyScreen(company: Company? = null) {
    val viewModel = remember { UpdateOrAddCompanyViewModel() }
    val editCompanyState = viewModel.editCompanyState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${company?.id ?: ""}")) }
    val nameValue = remember { mutableStateOf(TextFieldValue(company?.name ?: "")) }
    val countryValue = remember { mutableStateOf(TextFieldValue(company?.country ?: "")) }
    val emailValue = remember { mutableStateOf(TextFieldValue(company?.email ?: "")) }
    val addressValue = remember { mutableStateOf(TextFieldValue(company?.address ?: "")) }

    val isUpdating = company != null

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
            if (countryValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, countryText)
                return@launch
            }
            if (emailValue.value.text.isBlank()) {
                showInvalidValueSnackBar(scaffoldState, emailText)
                return@launch
            }
            lateinit var newCompany: Company

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val name = nameValue.value.text
            val country = countryValue.value.text
            val email = emailValue.value.text
            val address = addressValue.value.text

            if (isUpdating) {
                newCompany = company!!.copy(
                    name = name,
                    country = country,
                    email = email,
                    address = address
                )
                updateCompany(scope, viewModel, newCompany)
            } else {
                newCompany = Company(
                    id = id,
                    name = name,
                    country = country,
                    email = email,
                    address = address
                )
                addCompany(scope, viewModel, newCompany)
            }
        }
    }

    LaunchedEffect(viewModel.editCompanyState.collectAsState().value) {
        if (editCompanyState == EditCompanyState.Success) {
            showSuccess(scaffoldState, "Company", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { CompanyScreen() }
        } else if (editCompanyState is EditCompanyState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editCompanyState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Company" else "Add Company"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { CompanyScreen() }
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
        when (editCompanyState) {
            EditCompanyState.Loading -> Center {
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
                        value = countryValue.value,
                        onValueChange = { countryValue.value = it },
                        label = { Text(countryText) },
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


private fun addCompany(scope: CoroutineScope, viewModel: UpdateOrAddCompanyViewModel, company: Company) {
    scope.launch(Dispatchers.IO) { viewModel.addCompany(company) }
}

private fun updateCompany(scope: CoroutineScope, viewModel: UpdateOrAddCompanyViewModel, company: Company) {
    scope.launch(Dispatchers.IO) { viewModel.updateCompany(company) }
}