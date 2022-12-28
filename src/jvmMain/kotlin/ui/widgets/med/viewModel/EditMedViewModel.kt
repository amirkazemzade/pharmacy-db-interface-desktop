package ui.widgets.med.viewModel

import data.MysqlConnector
import data.model.Med
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddMedViewModel {
    private val _editMedState = MutableStateFlow<EditMedState>(EditMedState.Initial)
    val editMedState: StateFlow<EditMedState> = _editMedState

    fun addMed(med: Med) = runBlocking {
        _editMedState.value = EditMedState.Loading
        try {
            MysqlConnector.insertMed(med)
            _editMedState.value = EditMedState.Success
        } catch (e: Exception) {
            _editMedState.value = EditMedState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updateMed(med: Med) = runBlocking {
        try {
            _editMedState.value = EditMedState.Loading
            MysqlConnector.updateMed(med)
            _editMedState.value = EditMedState.Success
        } catch (e: Exception) {
            _editMedState.value = EditMedState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditMedState {
    object Initial : EditMedState()
    object Loading : EditMedState()
    object Success : EditMedState()
    data class Error(val error: String) : EditMedState()
}