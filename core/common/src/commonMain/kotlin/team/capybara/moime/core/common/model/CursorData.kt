package team.capybara.moime.core.common.model

import kotlinx.datetime.LocalDateTime
import team.capybara.moime.core.common.model.CursorRequest.Companion.DEFAULT_LIMIT

data class CursorData<T>(
    val data: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val nextCursorId: Int? = null,
    val nextCursorDate: LocalDateTime? = null,
    val isLast: Boolean? = null
) {
    fun canRequest() = !isLoading && isLast != true

    fun nextRequest(limit: Int = DEFAULT_LIMIT) = CursorRequest(nextCursorId, nextCursorDate, limit)

    fun loading(value: Boolean = true) = copy(isLoading = value)

    fun concatenate(next: CursorData<T>, reverse: Boolean = false) = copy(
        data = if (reverse) next.data + data else data + next.data,
        isLoading = false,
        nextCursorId = next.nextCursorId,
        nextCursorDate = next.nextCursorDate,
        isLast = next.isLast
    )
}
