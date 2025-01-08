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
