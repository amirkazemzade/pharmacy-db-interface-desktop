package ui.widgets.company.viewModel

import data.MysqlConnector
import data.model.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class UpdateOrAddCompanyViewModel {
    private val _editCompanyState = MutableStateFlow<EditCompanyState>(EditCompanyState.Initial)
    val editCompanyState: StateFlow<EditCompanyState> = _editCompanyState

    fun addCompany(company: Company) = runBlocking {
        _editCompanyState.value = EditCompanyState.Loading
        try {
            MysqlConnector.insertCompany(company)
            _editCompanyState.value = EditCompanyState.Success
        } catch (e: Exception) {
            _editCompanyState.value = EditCompanyState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }

    fun updateCompany(company: Company) = runBlocking {
        try {
            _editCompanyState.value = EditCompanyState.Loading
            MysqlConnector.updateCompany(company)
            _editCompanyState.value = EditCompanyState.Success
        } catch (e: Exception) {
            _editCompanyState.value = EditCompanyState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}

sealed class EditCompanyState {
    object Initial : EditCompanyState()
    object Loading : EditCompanyState()
    object Success : EditCompanyState()
    data class Error(val error: String) : EditCompanyState()
}