package ui.widgets.doctor.viewModel

import data.MysqlConnector
import data.model.Doctor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DoctorViewModel {
    private val _doctorsListState = MutableStateFlow<DoctorsListState>(DoctorsListState.Initial)
    val doctorsListState: StateFlow<DoctorsListState> = _doctorsListState

    fun getDoctors() = runBlocking {
        try {
            _doctorsListState.value = DoctorsListState.Loading
            val doctors = MysqlConnector.getDoctors()
            _doctorsListState.value = DoctorsListState.Success(doctors)
        } catch (e: Exception) {
            _doctorsListState.value = DoctorsListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DoctorsListState {
    object Initial : DoctorsListState()
    object Loading : DoctorsListState()

    data class Success(
        val doctors: List<Doctor>
    ) : DoctorsListState()

    data class Error(
        val error: String
    ) : DoctorsListState()
}