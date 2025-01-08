package team.capybara.moime.ui.model

import kotlinx.datetime.LocalDateTime

data class Friend(
    val id: Long,
    val code: String,
    val nickname: String,
    val profileImageUrl: String,
    val friendshipDateTime: LocalDateTime?,
    val blocked: Boolean
) {
    companion object {
        fun init(id: Long) = Friend(
            id = id,
            code = "",
            nickname = "",
            profileImageUrl = "",
            friendshipDateTime = null,
            blocked = false
        )
    }
}
