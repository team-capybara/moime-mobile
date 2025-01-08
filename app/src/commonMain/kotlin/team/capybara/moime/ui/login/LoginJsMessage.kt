package team.capybara.moime.ui.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginJsMessage(
    val accessToken: String,
    val isNewbie: Boolean
)
