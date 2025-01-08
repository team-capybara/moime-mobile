package team.capybara.moime.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import team.capybara.moime.data.Api
import team.capybara.moime.data.model.ApiException
import team.capybara.moime.ui.repository.NotificationRepository

class NotificationRepositoryImpl(
    private val httpClient: HttpClient
) : NotificationRepository {

    override suspend fun hasUnreadNotification(): Result<Boolean> = runCatching {
        httpClient.get(Api.NOTIFICATION_EXIST).run {
            if (status.value != 200) {
                throw ApiException(status)
            } else {
                body<Boolean>()
            }
        }
    }
}
