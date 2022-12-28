package ui.widgets.prescription.viewModel

import data.MysqlConnector
import data.model.Prescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class PrescriptionViewModel {
    private val _prescriptionsListState = MutableStateFlow<PrescriptionsListState>(PrescriptionsListState.Initial)
    val prescriptionsListState: StateFlow<PrescriptionsListState> = _prescriptionsListState

    fun getPrescriptions() = runBlocking {
        try {
            _prescriptionsListState.value = PrescriptionsListState.Loading
            val prescriptions = MysqlConnector.getPrescriptions()
            _prescriptionsListState.value = PrescriptionsListState.Success(prescriptions)
        } catch (e: Exception) {
            _prescriptionsListState.value = PrescriptionsListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class PrescriptionsListState {
    object Initial : PrescriptionsListState()
    object Loading : PrescriptionsListState()

    data class Success(
        val prescriptions: List<Prescription>
    ) : PrescriptionsListState()

    data class Error(
        val error: String
    ) : PrescriptionsListState()
}