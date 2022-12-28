package ui.widgets.doctor.viewModel

import data.MysqlConnector
import data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddDoctorViewModel {
    private val _editDoctorState = MutableStateFlow<EditDoctorState>(EditDoctorState.Initial)
    val editDoctorState: StateFlow<EditDoctorState> = _editDoctorState

    fun addDoctor(doctor: Doctor) = runBlocking {
        _editDoctorState.value = EditDoctorState.Loading
        try {
            MysqlConnector.insertDoctor(doctor)
            _editDoctorState.value = EditDoctorState.Success
        } catch (e: Exception) {
            _editDoctorState.value = EditDoctorState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updateDoctor(doctor: Doctor) = runBlocking {
        try {
            _editDoctorState.value = EditDoctorState.Loading
            MysqlConnector.updateDoctor(doctor)
            _editDoctorState.value = EditDoctorState.Success
        } catch (e: Exception) {
            _editDoctorState.value = EditDoctorState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditDoctorState {
    object Initial : EditDoctorState()
    object Loading : EditDoctorState()
    object Success : EditDoctorState()
    data class Error(val error: String) : EditDoctorState()
}