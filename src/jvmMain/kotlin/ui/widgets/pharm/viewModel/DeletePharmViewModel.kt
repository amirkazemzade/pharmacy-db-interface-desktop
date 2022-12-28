package ui.widgets.pharm.viewModel

import data.MysqlConnector
import data.model.Pharm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeletePharmViewModel {
    private val _deletePharmState = MutableStateFlow<DeletePharmState>(DeletePharmState.Initial)
    val deletePharmState: StateFlow<DeletePharmState> = _deletePharmState

    fun deletePharm(pharm: Pharm) = runBlocking {
        try {
            _deletePharmState.value = DeletePharmState.Loading
            MysqlConnector.deletePharm(pharm.id)
            _deletePharmState.value = DeletePharmState.Success
        } catch (e: Exception) {
            _deletePharmState.value = DeletePharmState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeletePharmState {
    object Initial : DeletePharmState()
    object Loading : DeletePharmState()
    object Success : DeletePharmState()
    data class Error(val error: String) : DeletePharmState()
}