package ui.widgets.doctor.viewModel

import data.MysqlConnector
import data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeleteDoctorViewModel {
    private val _deleteDoctorState = MutableStateFlow<DeleteDoctorState>(DeleteDoctorState.Initial)
    val deleteDoctorState: StateFlow<DeleteDoctorState> = _deleteDoctorState

    fun deleteDoctor(doctor: Doctor) = runBlocking {
        try {
            _deleteDoctorState.value = DeleteDoctorState.Loading
            MysqlConnector.deleteDoctor(doctor.id)
            _deleteDoctorState.value = DeleteDoctorState.Success
        } catch (e: Exception) {
            _deleteDoctorState.value = DeleteDoctorState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeleteDoctorState {
    object Initial : DeleteDoctorState()
    object Loading : DeleteDoctorState()
    object Success : DeleteDoctorState()
    data class Error(val error: String) : DeleteDoctorState()
}