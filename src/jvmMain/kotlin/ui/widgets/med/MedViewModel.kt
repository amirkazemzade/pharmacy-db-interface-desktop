package ui.widgets.med

import data.MysqlConnector
import data.model.Med
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class MedViewModel {
    private val _medsListState = MutableStateFlow<MedsListState>(MedsListState.Initial)
    val medsListState: StateFlow<MedsListState> = _medsListState

    fun getMeds() = runBlocking {
        try {
            _medsListState.value = MedsListState.Loading
            val meds = MysqlConnector.getMeds()
            _medsListState.value = MedsListState.Success(meds)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}


sealed class MedsListState {
    object Initial : MedsListState()
    object Loading : MedsListState()

    data class Success(
        val meds: List<Med>
    ) : MedsListState()

    data class Error(
        val error: String
    ) : MedsListState()
}