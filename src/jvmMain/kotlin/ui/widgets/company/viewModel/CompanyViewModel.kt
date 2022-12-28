package ui.widgets.company.viewModel

import data.MysqlConnector
import data.model.Company
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class CompanyViewModel {
    private val _companiesListState = MutableStateFlow<CompaniesListState>(CompaniesListState.Initial)
    val companiesListState: StateFlow<CompaniesListState> = _companiesListState

    fun getCompanies() = runBlocking {
        try {
            _companiesListState.value = CompaniesListState.Loading
            val companies = MysqlConnector.getCompanies()
            _companiesListState.value = CompaniesListState.Success(companies)
        } catch (e: Exception) {
            _companiesListState.value = CompaniesListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class CompaniesListState {
    object Initial : CompaniesListState()
    object Loading : CompaniesListState()

    data class Success(
        val companies: List<Company>
    ) : CompaniesListState()

    data class Error(
        val error: String
    ) : CompaniesListState()
}