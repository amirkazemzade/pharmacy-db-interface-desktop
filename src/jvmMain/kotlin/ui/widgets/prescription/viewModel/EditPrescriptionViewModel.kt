package ui.widgets.prescription.viewModel

import data.MysqlConnector
import data.model.Prescription
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddPrescriptionViewModel {
    private val _editPrescriptionState = MutableStateFlow<EditPrescriptionState>(EditPrescriptionState.Initial)
    val editPrescriptionState: StateFlow<EditPrescriptionState> = _editPrescriptionState

    fun addPrescription(prescription: Prescription) = runBlocking {
        _editPrescriptionState.value = EditPrescriptionState.Loading
        try {
            MysqlConnector.insertPrescription(prescription)
            _editPrescriptionState.value = EditPrescriptionState.Success
        } catch (e: Exception) {
            _editPrescriptionState.value = EditPrescriptionState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updatePrescription(prescription: Prescription) = runBlocking {
        try {
            _editPrescriptionState.value = EditPrescriptionState.Loading
            MysqlConnector.updatePrescription(prescription)
            _editPrescriptionState.value = EditPrescriptionState.Success
        } catch (e: Exception) {
            _editPrescriptionState.value = EditPrescriptionState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditPrescriptionState {
    object Initial : EditPrescriptionState()
    object Loading : EditPrescriptionState()
    object Success : EditPrescriptionState()
    data class Error(val error: String) : EditPrescriptionState()
}