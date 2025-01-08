package team.capybara.moime.ui.repository

import team.capybara.moime.ui.model.InsightSummary
import team.capybara.moime.ui.model.Survey

interface InsightRepository {
    suspend fun getInsightSummary(): Result<InsightSummary>

    suspend fun getSurvey(): Result<Survey>

    suspend fun postSurvey(): Result<Unit>
}
