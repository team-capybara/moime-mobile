package team.capybara.moime.ui.repository

interface NotificationRepository {

    suspend fun hasUnreadNotification(): Result<Boolean>
}
