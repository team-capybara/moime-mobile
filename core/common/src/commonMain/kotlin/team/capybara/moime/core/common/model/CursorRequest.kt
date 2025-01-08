package team.capybara.moime.core.common.model

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
