package ui.widgets.company.viewModel

import data.MysqlConnector
import data.model.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class DeleteCompanyViewModel {
    private val _deleteCompanyState = MutableStateFlow<DeleteCompanyState>(DeleteCompanyState.Initial)
    val deleteCompanyState: StateFlow<DeleteCompanyState> = _deleteCompanyState

    fun deleteCompany(company: Company) = runBlocking {
        try {
            _deleteCompanyState.value = DeleteCompanyState.Loading
            MysqlConnector.deleteCompany(company.id)
            _deleteCompanyState.value = DeleteCompanyState.Success
        } catch (e: Exception) {
            _deleteCompanyState.value = DeleteCompanyState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class DeleteCompanyState {
    object Initial : DeleteCompanyState()
    object Loading : DeleteCompanyState()
    object Success : DeleteCompanyState()
    data class Error(val error: String) : DeleteCompanyState()
}