package team.capybara.moime.feature.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginJsCallback(
    val fcmToken: String
)
