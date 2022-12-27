package ui.widgets.med

import data.MysqlConnector
import data.model.Med
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeleteMedViewModel {
    private val _deleteMedState = MutableStateFlow<DeleteMedState>(DeleteMedState.Initial)
    val deleteMedState: StateFlow<DeleteMedState> = _deleteMedState

    fun deleteMed(med: Med) = runBlocking {
        try {
            _deleteMedState.value = DeleteMedState.Loading
            MysqlConnector.deleteMed(med.id)
            _deleteMedState.value = DeleteMedState.Success
        } catch (e: Exception) {
            _deleteMedState.value = DeleteMedState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeleteMedState {
    object Initial : DeleteMedState()
    object Loading : DeleteMedState()
    object Success : DeleteMedState()
    data class Error(val error: String) : DeleteMedState()
}