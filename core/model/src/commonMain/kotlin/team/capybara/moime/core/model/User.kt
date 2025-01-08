package team.capybara.moime.core.model

data class User(
    val id: Long,
    val code: String,
    val nickname: String,
    val email: String,
    val providerType: ProviderType,
    val profileImageUrl: String
)
