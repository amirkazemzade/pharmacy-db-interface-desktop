package ui.widgets.category.viewModel

import data.MysqlConnector
import data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddCategoryViewModel {
    private val _editCategoryState = MutableStateFlow<EditCategoryState>(EditCategoryState.Initial)
    val editCategoryState: StateFlow<EditCategoryState> = _editCategoryState

    fun addCategory(category: Category) = runBlocking {
        _editCategoryState.value = EditCategoryState.Loading
        try {
            MysqlConnector.insertCategory(category)
            _editCategoryState.value = EditCategoryState.Success
        } catch (e: Exception) {
            _editCategoryState.value = EditCategoryState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updateCategory(category: Category) = runBlocking {
        try {
            _editCategoryState.value = EditCategoryState.Loading
            MysqlConnector.updateCategory(category)
            _editCategoryState.value = EditCategoryState.Success
        } catch (e: Exception) {
            _editCategoryState.value = EditCategoryState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditCategoryState {
    object Initial : EditCategoryState()
    object Loading : EditCategoryState()
    object Success : EditCategoryState()
    data class Error(val error: String) : EditCategoryState()
}