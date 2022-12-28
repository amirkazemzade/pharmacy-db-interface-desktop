package ui.widgets.category.viewModel

import data.MysqlConnector
import data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeleteCategoryViewModel {
    private val _deleteCategoryState = MutableStateFlow<DeleteCategoryState>(DeleteCategoryState.Initial)
    val deleteCategoryState: StateFlow<DeleteCategoryState> = _deleteCategoryState

    fun deleteCategory(category: Category) = runBlocking {
        try {
            _deleteCategoryState.value = DeleteCategoryState.Loading
            MysqlConnector.deleteCategory(category.id)
            _deleteCategoryState.value = DeleteCategoryState.Success
        } catch (e: Exception) {
            _deleteCategoryState.value = DeleteCategoryState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeleteCategoryState {
    object Initial : DeleteCategoryState()
    object Loading : DeleteCategoryState()
    object Success : DeleteCategoryState()
    data class Error(val error: String) : DeleteCategoryState()
}