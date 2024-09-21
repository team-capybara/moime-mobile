package ui.friend

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.cannot_find_friend
import moime.shared.generated.resources.cannot_find_friend_desc
import moime.shared.generated.resources.close
import moime.shared.generated.resources.confirm
import moime.shared.generated.resources.create_meeting
import moime.shared.generated.resources.failed_to_add_friend
import moime.shared.generated.resources.failed_to_add_friend_desc
import moime.shared.generated.resources.failed_to_unblock_friend
import moime.shared.generated.resources.failed_to_unblock_friend_desc
import moime.shared.generated.resources.friend_added
import moime.shared.generated.resources.friend_added_desc
import org.jetbrains.compose.resources.getString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.component.DialogRequest
import ui.model.CursorData
import ui.model.Friend
import ui.model.Stranger
import ui.repository.FriendRepository

class FriendScreenModel : StateScreenModel<FriendScreenModel.State>(State()), KoinComponent {

    private val friendRepository: FriendRepository by inject()

    data class State(
        val isFriendListLoading: Boolean = false,
        val friendsCount: Int = 0,
        val myFriends: CursorData<Friend> = CursorData(),
        val recommendedFriends: CursorData<Friend> = CursorData(),
        val searchedMyFriends: CursorData<Friend>? = null,
        val searchedRecommendedFriends: CursorData<Friend>? = null,
        val foundUser: Stranger? = null,
        val dialogRequest: DialogRequest? = null,
        val blockedFriendsCount: Int = 0,
        val blockedFriends: CursorData<Friend> = CursorData()
    )

    init {
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
                .onFailure { /* failed to get friend count */ }
        }
    }

    fun loadMyFriends() {
        if (state.value.myFriends.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            friendRepository.getMyFriends(
                cursor = state.value.myFriends.nextRequest() ?: return@launch
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    isFriendListLoading = false,
                    myFriends = state.value.myFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(
                    isFriendListLoading = false
                )
                /* failed to load my friends */
            }
        }
    }

    fun loadRecommendedFriends() {
        if (state.value.recommendedFriends.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            friendRepository.getRecommendedFriends(
                cursor = state.value.recommendedFriends.nextRequest() ?: return@launch
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    isFriendListLoading = false,
                    recommendedFriends = state.value.recommendedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(
                    isFriendListLoading = false
                )
                /* failed to load recommended friends */
            }
        }
    }

    fun searchMyFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedMyFriends()
            return
        }
        if (state.value.searchedMyFriends?.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            if (state.value.searchedMyFriends == null) {
                mutableState.value = state.value.copy(searchedMyFriends = CursorData())
            }
            friendRepository.getMyFriends(
                cursor = state.value.searchedMyFriends?.nextRequest() ?: return@launch
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    isFriendListLoading = false,
                    searchedMyFriends = state.value.searchedMyFriends?.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(
                    isFriendListLoading = false
                )
                /* failed to load searched my friends */
            }
        }
    }

    fun searchRecommendedFriends(nickname: String) {
        if (nickname.isBlank()) {
            clearSearchedRecommendedFriends()
            return
        }
        if (state.value.searchedRecommendedFriends?.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            if (state.value.searchedRecommendedFriends == null) {
                mutableState.value = state.value.copy(searchedRecommendedFriends = CursorData())
            }
            friendRepository.getRecommendedFriends(
                cursor = state.value.searchedRecommendedFriends?.nextRequest() ?: return@launch
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    isFriendListLoading = false,
                    searchedRecommendedFriends = state.value.searchedRecommendedFriends
                        ?.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(
                    isFriendListLoading = false
                )
                /* failed to load searched recommended friends */
            }
        }
    }

    fun findUser(code: String) {
        screenModelScope.launch {
            friendRepository.getFriend(code)
                .onSuccess { mutableState.value = state.value.copy(foundUser = it) }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.cannot_find_friend),
                            description = getString(Res.string.cannot_find_friend_desc),
                            actionTextRes = Res.string.confirm,
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

    fun addFriend(targetId: Long, nickname: String) {
        screenModelScope.launch {
            friendRepository.addFriend(targetId)
                .onSuccess {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.friend_added, nickname),
                            description = getString(Res.string.friend_added_desc, nickname),
                            actionTextRes = Res.string.create_meeting,
                            subActionTextRes = Res.string.close,
                            onAction = { /* 모임 생성 화면 이동 */ },
                            onSubAction = ::hideDialog
                        )
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_add_friend),
                            description = getString(Res.string.failed_to_add_friend_desc),
                            actionTextRes = Res.string.confirm,
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
                .onFailure { /* failed to get blocked friends count */ }
        }
    }

    fun loadBlockedFriends() {
        if (state.value.blockedFriends.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            friendRepository.getBlockedFriends(
                cursor = state.value.blockedFriends.nextRequest() ?: return@launch
            ).onSuccess { nextFriends ->
                mutableState.value = state.value.copy(
                    isFriendListLoading = false,
                    blockedFriends = state.value.blockedFriends.concatenate(nextFriends)
                )
            }.onFailure {
                mutableState.value = state.value.copy(
                    isFriendListLoading = false
                )
                /* failed to load blocked friends */
            }
        }
    }

    fun unblockFriend(targetId: Long) {
        screenModelScope.launch {
            friendRepository.unblockFriend(targetId)
                .onSuccess { /* success to unblock friend */ }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_unblock_friend),
                            description = getString(Res.string.failed_to_unblock_friend_desc),
                            actionTextRes = Res.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun showDialog(request: DialogRequest) {
        mutableState.value = state.value.copy(dialogRequest = request)
    }

    fun hideDialog() {
        mutableState.value = state.value.copy(dialogRequest = null)
    }
}
