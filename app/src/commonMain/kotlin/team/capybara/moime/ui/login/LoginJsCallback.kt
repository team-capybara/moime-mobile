package team.capybara.moime.ui.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginJsCallback(
    val fcmToken: String
)
