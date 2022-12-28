package ui.widgets.patient.viewModel

import data.MysqlConnector
import data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeletePatientViewModel {
    private val _deletePatientState = MutableStateFlow<DeletePatientState>(DeletePatientState.Initial)
    val deletePatientState: StateFlow<DeletePatientState> = _deletePatientState

    fun deletePatient(patient: Patient) = runBlocking {
        try {
            _deletePatientState.value = DeletePatientState.Loading
            MysqlConnector.deletePatient(patient.id)
            _deletePatientState.value = DeletePatientState.Success
        } catch (e: Exception) {
            _deletePatientState.value = DeletePatientState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeletePatientState {
    object Initial : DeletePatientState()
    object Loading : DeletePatientState()
    object Success : DeletePatientState()
    data class Error(val error: String) : DeletePatientState()
}