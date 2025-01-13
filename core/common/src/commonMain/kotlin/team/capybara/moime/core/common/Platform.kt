package team.capybara.moime.core.common

enum class Platform {
    Android,
    iOS,
}

expect fun getPlatform(): Platform

expect fun getVersionString(): String
