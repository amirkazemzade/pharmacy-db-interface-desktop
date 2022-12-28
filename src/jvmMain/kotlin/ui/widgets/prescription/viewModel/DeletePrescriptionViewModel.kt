package ui.widgets.prescription.viewModel

import data.MysqlConnector
import data.model.Prescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeletePrescriptionViewModel {
    private val _deletePrescriptionState = MutableStateFlow<DeletePrescriptionState>(DeletePrescriptionState.Initial)
    val deletePrescriptionState: StateFlow<DeletePrescriptionState> = _deletePrescriptionState

    fun deletePrescription(prescription: Prescription) = runBlocking {
        try {
            _deletePrescriptionState.value = DeletePrescriptionState.Loading
            MysqlConnector.deletePrescription(prescription.id)
            _deletePrescriptionState.value = DeletePrescriptionState.Success
        } catch (e: Exception) {
            _deletePrescriptionState.value = DeletePrescriptionState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeletePrescriptionState {
    object Initial : DeletePrescriptionState()
    object Loading : DeletePrescriptionState()
    object Success : DeletePrescriptionState()
    data class Error(val error: String) : DeletePrescriptionState()
}