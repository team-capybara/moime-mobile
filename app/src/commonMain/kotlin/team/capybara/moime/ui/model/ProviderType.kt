package team.capybara.moime.ui.model

enum class ProviderType(val value: String) {
    Kakao("KAKAO"),
    Apple("APPLE"),
    Unknown("UNKNOWN")
    ;

    companion object {
        fun from(type: String) = entries.find { it.value == type } ?: Unknown
    }
}
