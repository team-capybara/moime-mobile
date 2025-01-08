package team.capybara.moime.core.data.repository

import team.capybara.moime.core.model.User

interface UserRepository {
    suspend fun getUser(): Result<User>

    suspend fun login(accessToken: String): Result<Unit>

    suspend fun logout(): Result<Unit>
}
