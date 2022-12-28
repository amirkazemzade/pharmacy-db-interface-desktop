package ui.widgets.category.view

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
import data.model.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.navigator.LocalNavigator
import ui.widgets.category.viewModel.EditCategoryState
import ui.widgets.category.viewModel.UpdateOrAddCategoryViewModel
import ui.widgets.util.Center
import ui.widgets.util.showInvalidValueSnackBar
import ui.widgets.util.showSuccess
import util.*

@Preview
@Composable
fun EditCategoryScreen(category: Category? = null) {
    val viewModel = remember { UpdateOrAddCategoryViewModel() }
    val editCategoryState = viewModel.editCategoryState.collectAsState().value

    val navigator = LocalNavigator.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val idValue = remember { mutableStateOf(TextFieldValue("${category?.id ?: ""}")) }
    val nameValue = remember { mutableStateOf(TextFieldValue(category?.name ?: "")) }
    val descriptionValue = remember { mutableStateOf(TextFieldValue(category?.description ?: "")) }

    val isUpdating = category != null

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
            lateinit var newCategory: Category

            val id = if (idValue.value.text.isNotBlank()) idValue.value.text.toInt() else 0
            val name = nameValue.value.text
            val description = descriptionValue.value.text

            if (isUpdating) {
                newCategory = category!!.copy(
                    name = name,
                    description = description,
                )
                updateCategory(scope, viewModel, newCategory)
            } else {
                newCategory = Category(
                    id = id,
                    name = name,
                    description = description,
                )
                addCategory(scope, viewModel, newCategory)
            }
        }
    }

    LaunchedEffect(viewModel.editCategoryState.collectAsState().value) {
        if (editCategoryState == EditCategoryState.Success) {
            showSuccess(scaffoldState, "Category", if (isUpdating) Action.Update else Action.Add)
            delay(1000)
            navigator.invoke { CategoryScreen() }
        } else if (editCategoryState is EditCategoryState.Error) {
            scaffoldState.snackbarHostState.showSnackbar(editCategoryState.error)
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isUpdating) "Update Category" else "Add Category"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator.invoke { CategoryScreen() }
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
        when (editCategoryState) {
            EditCategoryState.Loading -> Center {
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
                        value = descriptionValue.value,
                        onValueChange = { descriptionValue.value = it },
                        label = { Text(descriptionText) },
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


private fun addCategory(scope: CoroutineScope, viewModel: UpdateOrAddCategoryViewModel, category: Category) {
    scope.launch(Dispatchers.IO) { viewModel.addCategory(category) }
}

private fun updateCategory(scope: CoroutineScope, viewModel: UpdateOrAddCategoryViewModel, category: Category) {
    scope.launch(Dispatchers.IO) { viewModel.updateCategory(category) }
}