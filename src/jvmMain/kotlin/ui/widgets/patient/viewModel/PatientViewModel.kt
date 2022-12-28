package ui.widgets.patient.viewModel

import data.MysqlConnector
import data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class PatientViewModel {
    private val _patientsListState = MutableStateFlow<PatientsListState>(PatientsListState.Initial)
    val patientsListState: StateFlow<PatientsListState> = _patientsListState

    fun getPatients() = runBlocking {
        try {
            _patientsListState.value = PatientsListState.Loading
            val patients = MysqlConnector.getPatients()
            _patientsListState.value = PatientsListState.Success(patients)
        } catch (e: Exception) {
            _patientsListState.value = PatientsListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class PatientsListState {
    object Initial : PatientsListState()
    object Loading : PatientsListState()

    data class Success(
        val patients: List<Patient>
    ) : PatientsListState()

    data class Error(
        val error: String
    ) : PatientsListState()
}