package ui.widgets.insurance.viewModel

import data.MysqlConnector
import data.model.Insurance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeleteInsuranceViewModel {
    private val _deleteInsuranceState = MutableStateFlow<DeleteInsuranceState>(DeleteInsuranceState.Initial)
    val deleteInsuranceState: StateFlow<DeleteInsuranceState> = _deleteInsuranceState

    fun deleteInsurance(insurance: Insurance) = runBlocking {
        try {
            _deleteInsuranceState.value = DeleteInsuranceState.Loading
            MysqlConnector.deleteInsurance(insurance.id)
            _deleteInsuranceState.value = DeleteInsuranceState.Success
        } catch (e: Exception) {
            _deleteInsuranceState.value = DeleteInsuranceState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeleteInsuranceState {
    object Initial : DeleteInsuranceState()
    object Loading : DeleteInsuranceState()
    object Success : DeleteInsuranceState()
    data class Error(val error: String) : DeleteInsuranceState()
}