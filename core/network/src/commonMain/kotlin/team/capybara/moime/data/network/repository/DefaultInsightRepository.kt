package team.capybara.moime.data.network.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import team.capybara.moime.core.data.repository.InsightRepository
import team.capybara.moime.core.model.InsightSummary
import team.capybara.moime.core.model.Survey
import team.capybara.moime.data.network.Api
import team.capybara.moime.data.network.model.InsightSummaryResponse
import team.capybara.moime.data.network.model.SurveyResponse

class DefaultInsightRepository(
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
