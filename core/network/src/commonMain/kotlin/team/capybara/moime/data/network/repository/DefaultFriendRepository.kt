package team.capybara.moime.data.network.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.data.repository.FriendRepository
import team.capybara.moime.core.model.Friend
import team.capybara.moime.data.network.Api
import team.capybara.moime.data.network.model.ApiException
import team.capybara.moime.data.network.model.FriendListResponse
import team.capybara.moime.data.network.model.FriendResponse

class DefaultFriendRepository(private val httpClient: HttpClient) : FriendRepository {

    override suspend fun getMyFriendsCount(): Result<Int> = runCatching {
        httpClient.get(Api.FRIENDS_COUNT).body<Int>()
    }

    override suspend fun getMyFriends(
        cursor: CursorRequest,
        nickname: String?
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_FOLLOWINGS) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
                nickname?.let { parameters.append("keyword", it) }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun getRecommendedFriends(
        cursor: CursorRequest,
        nickname: String?
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_RECOMMENDED) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
                nickname?.let { parameters.append("keyword", it) }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun getStranger(code: String): Result<Friend> = runCatching {
        httpClient.get(Api.USERS_FIND_CODE(code)) {
            url { parameters.append("userCode", code) }
        }.body<FriendResponse>().toUiModel()
    }

    override suspend fun getStranger(targetId: Long): Result<Friend> = runCatching {
        httpClient.get(Api.USERS_FIND_ID(targetId)) {
            url { parameters.append("userId", targetId.toString()) }
        }.body<FriendResponse>().toUiModel()
    }

    override suspend fun addFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.post(Api.FRIENDS_ADD) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }

    override suspend fun getBlockedFriendsCount(): Result<Int> = runCatching {
        httpClient.get(Api.FRIENDS_BLOCKED_COUNT).body<Int>()
    }

    override suspend fun getBlockedFriends(
        cursor: CursorRequest
    ): Result<CursorData<Friend>> = runCatching {
        httpClient.get(Api.FRIENDS_BLOCKED) {
            url {
                with(cursor) {
                    cursorId?.let { parameters.append("cursorId", it.toString()) }
                    limit?.let { parameters.append("size", it.toString()) }
                }
            }
        }.body<FriendListResponse>().run {
            CursorData(
                data = data.map { it.toUiModel() },
                nextCursorId = cursorId?.cursorId,
                isLast = last
            )
        }
    }

    override suspend fun blockFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.put(Api.FRIENDS_BLOCK) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }

    override suspend fun unblockFriend(targetId: Long): Result<Unit> = runCatching {
        httpClient.put(Api.FRIENDS_UNBLOCK) {
            url { parameters.append("targetId", targetId.toString()) }
        }.also { if (it.status.value != 200) throw ApiException(it.status) }
    }
}
