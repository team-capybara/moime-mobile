package team.capybara.moime.feature.mypage

import kotlinx.serialization.Serializable

/** Javascript Interface callback which return whether notification permission is granted. */
@Serializable
data class PermissionJsCallback(
    val granted: Boolean
)

/** Javascript Interface callback which return app version string. */
@Serializable
data class AppVersionJsCallback(
    val version: String
)
