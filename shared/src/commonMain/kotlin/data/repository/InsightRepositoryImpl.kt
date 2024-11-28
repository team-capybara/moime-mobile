package data.repository

import data.Api
import data.model.InsightSummaryResponse
import data.model.SurveyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import ui.model.InsightSummary
import ui.model.Survey
import ui.repository.InsightRepository

class InsightRepositoryImpl(
    private val httpClient: HttpClient
) : InsightRepository {

    override suspend fun getInsightSummary(): Result<InsightSummary> = runCatching {
        httpClient.get(Api.WEEKLY_SUMMARY).body<InsightSummaryResponse>().toUiModel()
    }

    override suspend fun getSurvey(): Result<Survey> = runCatching {
        httpClient.get(Api.SURVEY_FRIEND_STATS).body<SurveyResponse>().toUiModel()
    }

    override suspend fun postSurvey(): Result<Unit> = runCatching {
        httpClient.post(Api.SURVEY_FRIEND_STATS)
    }
}
