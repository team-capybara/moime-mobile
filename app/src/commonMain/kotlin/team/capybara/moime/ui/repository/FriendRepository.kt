package team.capybara.moime.ui.repository

import team.capybara.moime.ui.model.CursorData
import team.capybara.moime.ui.model.CursorRequest
import team.capybara.moime.ui.model.Friend

interface FriendRepository {

    suspend fun getMyFriendsCount(): Result<Int>

    suspend fun getMyFriends(
        cursor: CursorRequest,
        nickname: String? = null
    ): Result<CursorData<Friend>>

    suspend fun getRecommendedFriends(
        cursor: CursorRequest,
        nickname: String? = null
    ): Result<CursorData<Friend>>

    suspend fun getStranger(code: String): Result<Friend>

    suspend fun getStranger(targetId: Long): Result<Friend>

    suspend fun addFriend(targetId: Long): Result<Unit>

    suspend fun getBlockedFriendsCount(): Result<Int>

    suspend fun getBlockedFriends(cursor: CursorRequest): Result<CursorData<Friend>>

    suspend fun blockFriend(targetId: Long): Result<Unit>

    suspend fun unblockFriend(targetId: Long): Result<Unit>
}
