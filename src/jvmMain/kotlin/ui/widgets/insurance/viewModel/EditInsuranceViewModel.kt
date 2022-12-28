package ui.widgets.insurance.viewModel

import data.MysqlConnector
import data.model.Insurance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddInsuranceViewModel {
    private val _editInsuranceState = MutableStateFlow<EditInsuranceState>(EditInsuranceState.Initial)
    val editInsuranceState: StateFlow<EditInsuranceState> = _editInsuranceState

    fun addInsurance(insurance: Insurance) = runBlocking {
        _editInsuranceState.value = EditInsuranceState.Loading
        try {
            MysqlConnector.insertInsurance(insurance)
            _editInsuranceState.value = EditInsuranceState.Success
        } catch (e: Exception) {
            _editInsuranceState.value = EditInsuranceState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updateInsurance(insurance: Insurance) = runBlocking {
        try {
            _editInsuranceState.value = EditInsuranceState.Loading
            MysqlConnector.updateInsurance(insurance)
            _editInsuranceState.value = EditInsuranceState.Success
        } catch (e: Exception) {
            _editInsuranceState.value = EditInsuranceState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditInsuranceState {
    object Initial : EditInsuranceState()
    object Loading : EditInsuranceState()
    object Success : EditInsuranceState()
    data class Error(val error: String) : EditInsuranceState()
}