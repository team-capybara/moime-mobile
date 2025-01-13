/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
