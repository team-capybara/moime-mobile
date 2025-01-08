package team.capybara.moime.core.data.repository

import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Survey

interface InsightRepository {
    suspend fun getInsightSummary(): Result<InsightSummary>

    suspend fun getSurvey(): Result<Survey>

    suspend fun postSurvey(): Result<Unit>
}
