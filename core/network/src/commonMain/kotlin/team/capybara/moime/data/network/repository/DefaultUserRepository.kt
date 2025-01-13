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

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.get
import team.capybara.moime.core.common.ACCESS_TOKEN_KEY
import team.capybara.moime.core.common.model.BearerTokenStorage
import team.capybara.moime.core.data.repository.UserRepository
import team.capybara.moime.core.model.User
import team.capybara.moime.data.network.Api
import team.capybara.moime.data.network.model.UserResponse
import team.capybara.moime.data.network.model.toUser

class DefaultUserRepository(
    private val httpClient: HttpClient,
    private val settings: Settings,
    private val bearerTokenStorage: BearerTokenStorage
) : UserRepository {

    override suspend fun getUser(): Result<User> = runCatching {
        httpClient.get(Api.USERS_MY).body<UserResponse>().toUser()
    }

    override suspend fun login(accessToken: String): Result<Unit> = runCatching {
        settings.putString(ACCESS_TOKEN_KEY, accessToken)
        bearerTokenStorage.add(BearerTokens(accessToken, null))
    }

    override suspend fun logout(): Result<Unit> = runCatching {
        settings.remove(ACCESS_TOKEN_KEY)
        bearerTokenStorage.clear()
        httpClient.authProviders.forEach {
            if (it is BearerAuthProvider) {
                it.clearToken()
            }
        }
    }
}
