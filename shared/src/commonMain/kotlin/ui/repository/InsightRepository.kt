package ui.repository

import ui.model.InsightSummary
import ui.model.Survey

interface InsightRepository {
    suspend fun getInsightSummary(): Result<InsightSummary>

    suspend fun getSurvey(): Result<Survey>

    suspend fun postSurvey(): Result<Unit>
}
