package ui.main.insight

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ui.model.InsightSummary
import ui.repository.InsightRepository

class InsightScreenModel(
    private val insightRepository: InsightRepository
) : StateScreenModel<InsightScreenModel.State>(State.Init) {

    sealed interface State {
        data object Init : State
        data object Loading : State
        data class Success(val summary: InsightSummary) : State
        data class Failure(val throwable: Throwable) : State
    }

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value = State.Init
        getInsightSummary()
    }

    private fun getInsightSummary() {
        screenModelScope.launch {
            mutableState.value = State.Loading
            insightRepository.getInsightSummary()
                .onSuccess { mutableState.value = State.Success(it) }
                .onFailure { mutableState.value = State.Failure(it) }
        }
    }

    fun clearException() {
        mutableState.value = State.Init
    }
}
