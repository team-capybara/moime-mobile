package team.capybara.moime

enum class Platform {
    Android,
    iOS,
}

expect fun getPlatform(): Platform
