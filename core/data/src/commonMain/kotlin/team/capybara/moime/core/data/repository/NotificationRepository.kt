package team.capybara.moime.core.data.repository

interface NotificationRepository {

    suspend fun hasUnreadNotification(): Result<Boolean>
}
