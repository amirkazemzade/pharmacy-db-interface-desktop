package ui.widgets.pharm.view

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
import data.model.Pharm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.pharm.viewModel.EditPharmState
import ui.widgets.pharm.viewModel.UpdateOrAddPharmViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditPharmScreen(pharm: Pharm? = null) {
    val viewModel = remember { UpdateOrAddPharmViewModel() }
    val editPharmState = viewModel.editPharmState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${pharm?.id ?: ""}")) }
    val nameValue = remember { mutableStateOf(TextFieldValue(pharm?.name ?: "")) }
    val needPrcValue = remember { mutableStateOf(TextFieldValue("${pharm?.needPrescription?.toInt() ?: ""}")) }
    val usageValue = remember { mutableStateOf(TextFieldValue(pharm?.usage ?: "")) }
    val sideEffectsValue = remember { mutableStateOf(TextFieldValue(pharm?.sideEffects ?: "")) }
    val categoryIdValue = remember { mutableStateOf(TextFieldValue("${pharm?.categoryId ?: ""}")) }

    val isUpdating = pharm != null

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
            if (!needPrcValue.value.text.isIntBool()) {
                showInvalidValueSnackBar(scaffoldState, needPrcText)
                return@launch
            }
            if (!categoryIdValue.value.text.isNumeric()) {
                showInvalidValueSnackBar(scaffoldState, categoryIdText)
                return@launch
            }
            lateinit var newPharm: Pharm

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val name = nameValue.value.text
            val needPrc = needPrcValue.value.text.toInt().toBoolean()
            val usage = usageValue.value.text
            val sideEffects = sideEffectsValue.value.text
            val categoryId = categoryIdValue.value.text.toInt()

            if (isUpdating) {
                newPharm = pharm!!.copy(
                    name = name,
                    needPrescription = needPrc,
                    usage = usage,
                    sideEffects = sideEffects,
                    categoryId = categoryId,
                )
                updatePharm(scope, viewModel, newPharm)
            } else {
                newPharm = Pharm(
                    id = id,
                    name = name,
                    needPrescription = needPrc,
                    usage = usage,
                    sideEffects = sideEffects,
                    categoryId = categoryId,
                )
                addPharm(scope, viewModel, newPharm)
            }
        }
    }

    LaunchedEffect(viewModel.editPharmState.collectAsState().value) {
        if (editPharmState == EditPharmState.Success) {
            showSuccess(scaffoldState, "Pharm", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { PharmScreen() }
        } else if (editPharmState is EditPharmState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editPharmState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Pharm" else "Add Pharm"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { PharmScreen() }
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
        when (editPharmState) {
            EditPharmState.Loading -> Center {
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
                        value = needPrcValue.value,
                        onValueChange = { needPrcValue.value = it },
                        label = { Text(needPrcText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = usageValue.value,
                        onValueChange = { usageValue.value = it },
                        label = { Text(usageText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = sideEffectsValue.value,
                        onValueChange = { sideEffectsValue.value = it },
                        label = { Text(sideEffectsText) },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1
                    )
                    OutlinedTextField(
                        value = categoryIdValue.value,
                        onValueChange = { categoryIdValue.value = it },
                        label = { Text(categoryIdText) },
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


private fun addPharm(scope: CoroutineScope, viewModel: UpdateOrAddPharmViewModel, pharm: Pharm) {
    scope.launch(Dispatchers.IO) { viewModel.addPharm(pharm) }
}

private fun updatePharm(scope: CoroutineScope, viewModel: UpdateOrAddPharmViewModel, pharm: Pharm) {
    scope.launch(Dispatchers.IO) { viewModel.updatePharm(pharm) }
}