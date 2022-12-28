package ui.widgets.insurance.viewModel

import data.MysqlConnector
import data.model.Insurance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class InsuranceViewModel {
    private val _insurancesListState = MutableStateFlow<InsurancesListState>(InsurancesListState.Initial)
    val insurancesListState: StateFlow<InsurancesListState> = _insurancesListState

    fun getInsurances() = runBlocking {
        try {
            _insurancesListState.value = InsurancesListState.Loading
            val insurances = MysqlConnector.getInsurances()
            _insurancesListState.value = InsurancesListState.Success(insurances)
        } catch (e: Exception) {
            _insurancesListState.value = InsurancesListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class InsurancesListState {
    object Initial : InsurancesListState()
    object Loading : InsurancesListState()

    data class Success(
        val insurances: List<Insurance>
    ) : InsurancesListState()

    data class Error(
        val error: String
    ) : InsurancesListState()
}