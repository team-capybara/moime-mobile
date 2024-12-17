package data.repository

import com.mmk.kmpnotifier.notification.NotifierManager
import com.russhwolf.settings.Settings
import data.Api
import data.model.UserResponse
import data.model.toUser
import di.BearerTokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent
import ui.jsbridge.ACCESS_TOKEN_KEY
import ui.model.User
import ui.repository.UserRepository

class UserRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: Settings,
    private val bearerTokenStorage: BearerTokenStorage
) : UserRepository, KoinComponent {

    override suspend fun getUser(): Result<User> = runCatching {
        httpClient.get(Api.USERS_MY).body<UserResponse>().toUser()
    }

    override suspend fun login(accessToken: String): Result<String?> = runCatching {
        settings.putString(ACCESS_TOKEN_KEY, accessToken)
        bearerTokenStorage.add(BearerTokens(accessToken, null))
        NotifierManager.getPushNotifier().getToken()
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
