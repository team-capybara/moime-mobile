package team.capybara.moime.data.network.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import team.capybara.moime.core.data.repository.NotificationRepository
import team.capybara.moime.data.network.Api
import team.capybara.moime.data.network.model.ApiException

class DefaultNotificationRepository(
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
