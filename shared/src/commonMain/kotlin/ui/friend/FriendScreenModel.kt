package ui.friend

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import team.capybara.moime.SharedRes
import ui.component.DialogRequest
import ui.model.CursorData
import ui.model.Friend
import ui.model.Stranger
import ui.repository.FriendRepository

class FriendScreenModel : StateScreenModel<FriendScreenModel.State>(State()), KoinComponent {

    private val friendRepository: FriendRepository by inject()

    data class State(
        val isFriendListLoading: Boolean = false,
        val friendCount: Int = 0,
        val myFriends: CursorData<Friend> = CursorData(),
        val recommendedFriends: CursorData<Friend> = CursorData(),
        val searchedMyFriends: CursorData<Friend>? = null,
        val searchedRecommendedFriends: CursorData<Friend>? = null,
        val foundUser: Stranger? = null,
        val dialogRequest: DialogRequest? = null
    )

    init {
        getFriendCount()
        loadMyFriends()
        loadRecommendedFriends()
    }

    private fun getFriendCount() {
        screenModelScope.launch {
            val friendCount = friendRepository.getMyFriendsCount()
            mutableState.value = state.value.copy(
                friendCount = friendCount
            )
        }
    }

    fun loadMyFriends() {
        if (state.value.myFriends.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            val nextFriends = friendRepository.getMyFriends(
                cursor = state.value.myFriends.nextRequest() ?: return@launch
            )
            mutableState.value = state.value.copy(
                isFriendListLoading = false,
                myFriends = state.value.myFriends.concatenate(nextFriends)
            )
        }
    }

    fun loadRecommendedFriends() {
        if (state.value.recommendedFriends.isLast == true || state.value.isFriendListLoading) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(isFriendListLoading = true)
            val nextFriends = friendRepository.getRecommendedFriends(
                cursor = state.value.recommendedFriends.nextRequest() ?: return@launch
            )
            mutableState.value = state.value.copy(
                isFriendListLoading = false,
                recommendedFriends = state.value.recommendedFriends.concatenate(nextFriends)
            )
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
            val nextFriends = friendRepository.getMyFriends(
                cursor = state.value.searchedMyFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            )
            mutableState.value = state.value.copy(
                isFriendListLoading = false,
                searchedMyFriends = state.value.searchedMyFriends?.concatenate(nextFriends)
            )
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
            val nextFriends = friendRepository.getRecommendedFriends(
                cursor = state.value.searchedRecommendedFriends?.nextRequest() ?: return@launch,
                nickname = nickname
            )
            mutableState.value = state.value.copy(
                isFriendListLoading = false,
                searchedRecommendedFriends = state.value.searchedRecommendedFriends?.concatenate(nextFriends)
            )
        }
    }

    fun foundUser(code: String) {
        screenModelScope.launch {
            val foundUser = friendRepository.getFriend(code)
            if (foundUser == null) {
                showDialog(
                    DialogRequest(
                        title = "친구를 찾을 수 없습니다",
                        subtitle = "코드를 다시 입력해주세요",
                        actionRes = SharedRes.strings.confirm,
                        onAction = ::hideDialog
                    )
                )
            } else {
                mutableState.value = state.value.copy(foundUser = foundUser)
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

    fun showDialog(request: DialogRequest) {
        mutableState.value = state.value.copy(dialogRequest = request)
    }

    fun hideDialog() {
        mutableState.value = state.value.copy(dialogRequest = null)
    }
}
