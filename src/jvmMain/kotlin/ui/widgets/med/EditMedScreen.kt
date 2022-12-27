package ui.widgets.med

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.model.Med
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditMedScreen(med: Med? = null) {
    val viewModel = remember { UpdateOrAddMedViewModel() }
    val editMedState = viewModel.editMedState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${med?.id ?: ""}")) }
    val pharmIdValue = remember { mutableStateOf(TextFieldValue("${med?.pharmId ?: ""}")) }
    val compIdValue = remember { mutableStateOf(TextFieldValue("${med?.compId ?: ""}")) }
    val invValue = remember { mutableStateOf(TextFieldValue("${med?.inv ?: ""}")) }
    val priceValue = remember { mutableStateOf(TextFieldValue("${med?.price ?: ""}")) }
    val expirationDateValue = remember { mutableStateOf(TextFieldValue("${med?.expirationDate ?: ""}")) }
    val medNameValue = remember { mutableStateOf(TextFieldValue(med?.medName ?: "")) }

    val isUpdating = med != null

    val onDone = {
        scope.launch {
            if (!isUpdating && idValue.value.text.isNotBlank() && !idValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, idText)
                return@launch
            }
            if (!pharmIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, pharmIdText)
                return@launch
            }
            if (!compIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, compIdText)
                return@launch
            }
            if (!invValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, inventoryText)
                return@launch
            }
            if (!priceValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, priceText)
                return@launch
            }
            if (expirationDateValue.value.text.isBlank() || !expirationDateValue.value.text.isDate()) {
                showInvalidValueSnackBar(scaffoldState, expirationDateText)
                return@launch
            }
            lateinit var newMed: Med

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val pharmId = pharmIdValue.value.text.toInt()
            val compId = compIdValue.value.text.toInt()
            val inv = invValue.value.text.toInt()
            val price = priceValue.value.text.toLong()
            val expirationDate = expirationDateValue.value.text.toDate()
            val medName = medNameValue.value.text

            if (isUpdating) {
                newMed = med!!.copy(
                    pharmId = pharmId,
                    compId = compId,
                    inv = inv,
                    price = price,
                    expirationDate = expirationDate,
                    medName = medName,
                )
                updateMed(scope, viewModel, newMed)
            } else {
                newMed = Med(
                    id = id,
                    pharmId = pharmId,
                    compId = compId,
                    inv = inv,
                    price = price,
                    expirationDate = expirationDate,
                    medName = medName,
                )
                addMed(scope, viewModel, newMed)
            }
        }
    }

    LaunchedEffect(editMedState) {
        if (editMedState == EditMedState.Success) {
            showSuccess(scaffoldState, "Med", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { MedScreen() }
        } else if (editMedState is EditMedState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editMedState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Med" else "Add Med"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { MedScreen() }
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
        when (editMedState) {
            EditMedState.Loading -> Center {
                CircularProgressIndicator()
            }

            is EditMedState.Error -> Text(editMedState.error, color = Color.Red)
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
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = pharmIdValue.value,
                        onValueChange = { pharmIdValue.value = it },
                        label = { Text(pharmIdText) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = compIdValue.value,
                        onValueChange = { compIdValue.value = it },
                        label = { Text(compIdText) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = invValue.value,
                        onValueChange = { invValue.value = it },
                        label = { Text(inventoryText) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = priceValue.value,
                        onValueChange = { priceValue.value = it },
                        label = { Text(priceText) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = expirationDateValue.value,
                        onValueChange = { expirationDateValue.value = it },
                        label = { Text(expirationDateText) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = medNameValue.value,
                        onValueChange = { medNameValue.value = it },
                        label = { Text(medNameText) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    )
                }

            }
        }
    }
}


private fun addMed(scope: CoroutineScope, viewModel: UpdateOrAddMedViewModel, med: Med) {
    scope.launch(Dispatchers.IO) { viewModel.addMed(med) }
}

private fun updateMed(scope: CoroutineScope, viewModel: UpdateOrAddMedViewModel, med: Med) {
    scope.launch(Dispatchers.IO) { viewModel.updateMed(med) }
}