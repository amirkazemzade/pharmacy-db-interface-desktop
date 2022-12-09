package ui.dashboard

import data.MysqlConnector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class DashboardViewModel {

    private val _countMedsState = MutableStateFlow<CountMedsState>(CountMedsState.Initial)
    val countMedsState: StateFlow<CountMedsState> = _countMedsState

    suspend fun countMeds() = runBlocking {
        this.launch(Dispatchers.IO) {
            _countMedsState.value = CountMedsState.Loading
            delay(5000)
            val medCount = MysqlConnector.medCount()
            _countMedsState.value = CountMedsState.Success(medCount)
        }
    }
}

sealed class CountMedsState {
    object Initial : CountMedsState()

    object Loading : CountMedsState()

    data class Success(
        val medsCount: Int
    ) : CountMedsState()

    data class Error(
        val error: String
    ) : CountMedsState()
}