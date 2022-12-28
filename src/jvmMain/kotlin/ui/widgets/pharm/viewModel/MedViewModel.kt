package ui.widgets.pharm.viewModel

import data.MysqlConnector
import data.model.Pharm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import util.defaultErrorMessage

class PharmViewModel {
    private val _pharmsListState = MutableStateFlow<PharmsListState>(PharmsListState.Initial)
    val pharmsListState: StateFlow<PharmsListState> = _pharmsListState

    fun getPharms() = runBlocking {
        try {
            _pharmsListState.value = PharmsListState.Loading
            val pharms = MysqlConnector.getPharms()
            _pharmsListState.value = PharmsListState.Success(pharms)
        } catch (e: Exception) {
            _pharmsListState.value = PharmsListState.Error(e.message ?: defaultErrorMessage)
            e.printStackTrace()
        }
    }
}


sealed class PharmsListState {
    object Initial : PharmsListState()
    object Loading : PharmsListState()

    data class Success(
        val pharms: List<Pharm>
    ) : PharmsListState()

    data class Error(
        val error: String
    ) : PharmsListState()
}