package team.capybara.moime.ui.model

import kotlinx.datetime.LocalDateTime

data class CursorRequest(
    val cursorId: Int?,
    val cursorDate: LocalDateTime? = null,
    val limit: Int? = DEFAULT_LIMIT
) {
    companion object {
        const val DEFAULT_LIMIT = 5
    }
}
