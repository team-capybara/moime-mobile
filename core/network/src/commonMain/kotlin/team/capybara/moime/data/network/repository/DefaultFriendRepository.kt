/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
