package ui.widgets.category.viewModel

import data.MysqlConnector
import data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class CategoryViewModel {
    private val _categorysListState = MutableStateFlow<CategorysListState>(CategorysListState.Initial)
    val categorysListState: StateFlow<CategorysListState> = _categorysListState

    fun getCategorys() = runBlocking {
        try {
            _categorysListState.value = CategorysListState.Loading
            val categorys = MysqlConnector.getCategories()
            _categorysListState.value = CategorysListState.Success(categorys)
        } catch (e: Exception) {
            _categorysListState.value = CategorysListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class CategorysListState {
    object Initial : CategorysListState()
    object Loading : CategorysListState()

    data class Success(
        val categorys: List<Category>
    ) : CategorysListState()

    data class Error(
        val error: String
    ) : CategorysListState()
}