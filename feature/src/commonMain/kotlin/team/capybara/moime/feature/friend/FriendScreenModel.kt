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

package team.capybara.moime.feature.friend

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.close
import moime.core.designsystem.generated.resources.confirm
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.cannot_find_friend
import moime.feature.generated.resources.cannot_find_friend_desc
import moime.feature.generated.resources.create_meeting
import moime.feature.generated.resources.failed_to_add_friend
import moime.feature.generated.resources.failed_to_add_friend_desc
import moime.feature.generated.resources.failed_to_unblock_friend
import moime.feature.generated.resources.failed_to_unblock_friend_desc
import moime.feature.generated.resources.friend_added
import moime.feature.generated.resources.friend_added_desc
import org.jetbrains.compose.resources.getString
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.data.repository.FriendRepository
import team.capybara.moime.core.designsystem.component.DialogRequest
import team.capybara.moime.core.model.Friend
import moime.core.designsystem.generated.resources.Res as MoimeRes

class FriendScreenModel(
    private val friendRepository: FriendRepository
) : StateScreenModel<FriendScreenModel.State>(State()) {

    data class State(
        val friendsCount: Int = 0,
        val myFriends: CursorData<Friend> = CursorData(),
        val recommendedFriends: CursorData<Friend> = CursorData(),
        val searchedMyFriends: CursorData<Friend>? = null,
        val searchedRecommendedFriends: CursorData<Friend>? = null,
        val foundUser: Friend? = null,
        val dialogRequest: DialogRequest? = null,
        val blockedFriendsCount: Int = 0,
        val blockedFriends: CursorData<Friend> = CursorData(),
        val exception: Throwable? = null
    )

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value = State()
        getFriendsCount()
        getBlockedFriendsCount()
        loadMyFriends()
        loadRecommendedFriends()
        loadBlockedFriends()
    }

    private fun getFriendsCount() {
        screenModelScope.launch {
            friendRepository.getMyFriendsCount()
                .onSuccess { mutableState.value = state.value.copy(friendsCount = it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    fun loadMyFriends() {
        if (state.value.myFriends.canRequest().not()) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(myFriends = state.value.myFriends.loading())
            friendRepository.getMyFriends(
                cursor = state.value.myFriends.nextRequest()
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    myFriends = state.value.myFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(exception = it)
            }
        }
    }

    fun loadRecommendedFriends() {
        if (state.value.recommendedFriends.canRequest().not()) return
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(recommendedFriends = state.value.recommendedFriends.loading())
            friendRepository.getRecommendedFriends(
                cursor = state.value.recommendedFriends.nextRequest()
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    recommendedFriends = state.value.recommendedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(exception = it)
            }
        }
    }

    fun searchMyFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedMyFriends()
            return
        }
        if (state.value.searchedMyFriends?.canRequest() == false) return
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(searchedMyFriends = state.value.searchedMyFriends?.loading())
            if (state.value.searchedMyFriends == null) {
                mutableState.value = state.value.copy(searchedMyFriends = CursorData())
            }
            friendRepository.getMyFriends(
                cursor = state.value.searchedMyFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    searchedMyFriends = state.value.searchedMyFriends?.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(exception = it)
            }
        }
    }

    fun searchRecommendedFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedRecommendedFriends()
            return
        }
        if (state.value.searchedRecommendedFriends?.canRequest() == false) return
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(searchedRecommendedFriends = state.value.searchedRecommendedFriends?.loading())
            if (state.value.searchedRecommendedFriends == null) {
                mutableState.value = state.value.copy(searchedRecommendedFriends = CursorData())
            }
            friendRepository.getRecommendedFriends(
                cursor = state.value.searchedRecommendedFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    searchedRecommendedFriends = state.value.searchedRecommendedFriends
                        ?.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(exception = it)
            }
        }
    }

    fun findUser(code: String) {
        screenModelScope.launch {
            friendRepository.getStranger(code)
                .onSuccess { mutableState.value = state.value.copy(foundUser = it) }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.cannot_find_friend),
                            description = getString(Res.string.cannot_find_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun clearSearchedMyFriends() {
        mutableState.value = state.value.copy(searchedMyFriends = null)
    }

    fun clearSearchedRecommendedFriends() {
        mutableState.value = state.value.copy(searchedRecommendedFriends = null)
    }

    fun clearFoundUser() {
        mutableState.value = state.value.copy(foundUser = null)
    }

    fun addFriend(target: Friend, action: () -> Unit) {
        screenModelScope.launch {
            friendRepository.addFriend(target.id)
                .onSuccess {
                    clearFoundUser()
                    mutableState.value = state.value.copy(
                        friendsCount = state.value.friendsCount + 1,
                        myFriends = state.value.myFriends.copy(
                            data = listOf(target) + state.value.myFriends.data
                        ),
                        recommendedFriends = state.value.recommendedFriends.copy(
                            data = state.value.recommendedFriends.data.filter { it.id != target.id }
                        )
                    )

                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.friend_added, target.nickname),
                            description = getString(Res.string.friend_added_desc, target.nickname),
                            actionTextRes = Res.string.create_meeting,
                            subActionTextRes = MoimeRes.string.close,
                            onAction = action,
                            onSubAction = ::hideDialog
                        )
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_add_friend),
                            description = getString(Res.string.failed_to_add_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    private fun getBlockedFriendsCount() {
        screenModelScope.launch {
            friendRepository.getBlockedFriendsCount()
                .onSuccess { mutableState.value = state.value.copy(blockedFriendsCount = it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    fun loadBlockedFriends() {
        if (state.value.blockedFriends.canRequest().not()) return
        screenModelScope.launch {
            mutableState.value =
                state.value.copy(blockedFriends = state.value.blockedFriends.loading())
            friendRepository.getBlockedFriends(
                cursor = state.value.blockedFriends.nextRequest()
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    blockedFriends = state.value.blockedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(exception = it)
            }
        }
    }

    fun unblockFriend(targetId: Long) {
        screenModelScope.launch {
            friendRepository.unblockFriend(targetId)
                .onSuccess {
                    mutableState.value = state.value.copy(
                        blockedFriends = state.value.blockedFriends.copy(
                            data = state.value.blockedFriends.data.filter { it.id != targetId }
                        ),
                        blockedFriendsCount = state.value.blockedFriendsCount - 1
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_unblock_friend),
                            description = getString(Res.string.failed_to_unblock_friend_desc),
                            actionTextRes = MoimeRes.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    private fun showDialog(request: DialogRequest) {
        mutableState.value = state.value.copy(dialogRequest = request)
    }

    fun hideDialog() {
        mutableState.value = state.value.copy(dialogRequest = null)
    }

    fun clearException() {
        mutableState.value = state.value.copy(exception = null)
    }
}
