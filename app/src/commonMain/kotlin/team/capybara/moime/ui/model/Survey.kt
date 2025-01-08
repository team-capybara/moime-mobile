package team.capybara.moime.ui.model

data class Survey(
    val likeCount: Int,
    val submitted: Boolean
) {
    fun submit() = if (submitted) this else copy(likeCount = likeCount + 1, submitted = true)
}
