package ui.repository

import ui.model.User

interface UserRepository {
    suspend fun getUser(): Result<User>

    /**
     * @return FCM Token
     */
    suspend fun login(accessToken: String): Result<String?>

    suspend fun logout(): Result<Unit>
}
