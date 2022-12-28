package ui.widgets.patient.viewModel

import data.MysqlConnector
import data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddPatientViewModel {
    private val _editPatientState = MutableStateFlow<EditPatientState>(EditPatientState.Initial)
    val editPatientState: StateFlow<EditPatientState> = _editPatientState

    fun addPatient(patient: Patient) = runBlocking {
        _editPatientState.value = EditPatientState.Loading
        try {
            MysqlConnector.insertPatient(patient)
            _editPatientState.value = EditPatientState.Success
        } catch (e: Exception) {
            _editPatientState.value = EditPatientState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updatePatient(patient: Patient) = runBlocking {
        try {
            _editPatientState.value = EditPatientState.Loading
            MysqlConnector.updatePatient(patient)
            _editPatientState.value = EditPatientState.Success
        } catch (e: Exception) {
            _editPatientState.value = EditPatientState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditPatientState {
    object Initial : EditPatientState()
    object Loading : EditPatientState()
    object Success : EditPatientState()
    data class Error(val error: String) : EditPatientState()
}