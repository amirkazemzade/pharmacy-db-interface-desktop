package ui.widgets.pharm.viewModel

import data.MysqlConnector
import data.model.Pharm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddPharmViewModel {
    private val _editPharmState = MutableStateFlow<EditPharmState>(EditPharmState.Initial)
    val editPharmState: StateFlow<EditPharmState> = _editPharmState

    fun addPharm(pharm: Pharm) = runBlocking {
        _editPharmState.value = EditPharmState.Loading
        try {
            MysqlConnector.insertPharm(pharm)
            _editPharmState.value = EditPharmState.Success
        } catch (e: Exception) {
            _editPharmState.value = EditPharmState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updatePharm(pharm: Pharm) = runBlocking {
        try {
            _editPharmState.value = EditPharmState.Loading
            MysqlConnector.updatePharm(pharm)
            _editPharmState.value = EditPharmState.Success
        } catch (e: Exception) {
            _editPharmState.value = EditPharmState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditPharmState {
    object Initial : EditPharmState()
    object Loading : EditPharmState()
    object Success : EditPharmState()
    data class Error(val error: String) : EditPharmState()
}