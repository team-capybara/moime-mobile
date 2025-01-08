package team.capybara.moime.data.network.model

import kotlinx.serialization.Serializable
import team.capybara.moime.core.model.ProviderType
import team.capybara.moime.core.model.User

@Serializable
data class UserResponse(
    val id: Long,
    val code: String,
    val nickname: String,
    val email: String,
    val providerType: String,
    val profile: String
)

fun UserResponse.toUser() = User(
    id = id,
    code = code,
    nickname = nickname,
    email = email,
    providerType = ProviderType.from(providerType),
    profileImageUrl = profile
)
