package team.capybara.moime.data.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import team.capybara.moime.data.util.DateUtil.toIsoDateTimeFormat
import team.capybara.moime.ui.model.Friend

@Serializable
data class FriendListResponse(
    val data: List<FriendResponse>,
    val last: Boolean,
    val cursorId: Cursor?
)

@Serializable
data class FriendResponse(
    val id: Long,
    val code: String,
    val nickname: String,
    val profile: String,
    val friendshipDate: String?,
    val blocked: Boolean
) {
    fun toUiModel() = Friend(
        id = id,
        code = code,
        nickname = nickname,
        profileImageUrl = profile,
        friendshipDateTime = friendshipDate?.let { LocalDateTime.parse(it.toIsoDateTimeFormat()) },
        blocked = blocked
    )
}

@Serializable
data class Cursor(
    val cursorId: Int
)
