package team.capybara.moime.feature.insight

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import team.capybara.moime.core.data.repository.InsightRepository
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Survey

class InsightScreenModel(
    private val insightRepository: InsightRepository
) : StateScreenModel<InsightScreenModel.State>(State.Init) {

    sealed interface State {
        data object Init : State
        data object Loading : State
        data class Success(
            val summary: InsightSummary,
            val survey: Survey = Survey(100, false)
        ) : State

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
            val summary = insightRepository.getInsightSummary().getOrElse {
                mutableState.value = State.Failure(it)
                null
            }
            val survey = insightRepository.getSurvey().getOrElse {
                mutableState.value = State.Failure(it)
                null
            }
            if (summary != null && survey != null) {
                mutableState.value = State.Success(summary, survey)
            }
        }
    }

    fun postSurvey() {
        screenModelScope.launch {
            when (val state = state.value) {
                is State.Success -> {
                    if (state.survey.submitted.not()) insightRepository.postSurvey()
                    mutableState.value = state.copy(survey = state.survey.submit())
                }

                else -> return@launch
            }
        }
    }

    fun clearException() {
        mutableState.value = State.Init
    }
}
