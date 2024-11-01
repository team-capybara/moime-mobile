package ui.friend

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.block
import moime.shared.generated.resources.block_friend_dialog
import moime.shared.generated.resources.block_friend_dialog_desc
import moime.shared.generated.resources.cancel
import moime.shared.generated.resources.close
import moime.shared.generated.resources.confirm
import moime.shared.generated.resources.create_meeting
import moime.shared.generated.resources.failed_to_add_friend
import moime.shared.generated.resources.failed_to_add_friend_desc
import moime.shared.generated.resources.failed_to_block_friend
import moime.shared.generated.resources.failed_to_block_friend_desc
import moime.shared.generated.resources.failed_to_unblock_friend
import moime.shared.generated.resources.failed_to_unblock_friend_desc
import moime.shared.generated.resources.friend_added
import moime.shared.generated.resources.friend_added_desc
import moime.shared.generated.resources.unblock
import moime.shared.generated.resources.unblock_friend_dialog
import moime.shared.generated.resources.unblock_friend_dialog_desc
import org.jetbrains.compose.resources.getString
import ui.component.DialogRequest
import ui.model.CursorData
import ui.model.Friend
import ui.model.Meeting
import ui.repository.FriendRepository
import ui.repository.MeetingRepository

class FriendDetailScreenModel(
    private val targetId: Long,
    private val friendScreenModel: FriendScreenModel,
    private val friendRepository: FriendRepository,
    private val meetingRepository: MeetingRepository
) : StateScreenModel<FriendDetailScreenModel.State>(State(Friend.init(targetId))) {

    data class State(
        val stranger: Friend,
        val meetings: CursorData<Meeting> = CursorData(),
        val meetingsTotalCount: Int = 0,
        val dialogRequest: DialogRequest? = null,
        val exception: Throwable? = null
    )

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value = State(Friend.init(targetId))
        getStranger()
        getMeetingsTotalCount()
        loadMeetings()
    }

    private fun getStranger() {
        screenModelScope.launch {
            friendRepository.getStranger(targetId)
                .onSuccess { stranger ->
                    mutableState.value = state.value.copy(stranger = stranger)
                }
                .onFailure {
                    mutableState.value = state.value.copy(exception = it)
                }
        }
    }

    private fun getMeetingsTotalCount() {
        screenModelScope.launch {
            meetingRepository.getMeetingsCountWith(targetId)
                .onSuccess { totalCount ->
                    mutableState.value = state.value.copy(meetingsTotalCount = totalCount)
                }
                .onFailure {
                    mutableState.value = state.value.copy(exception = it)
                }
        }
    }

    fun loadMeetings() {
        if (state.value.meetings.canRequest().not()) return
        screenModelScope.launch {
            mutableState.value = state.value.copy(meetings = state.value.meetings.loading())
            meetingRepository.getMeetingsWith(targetId, state.value.meetings.nextRequest())
                .onSuccess { nextMeetings ->
                    mutableState.value = state.value.copy(
                        meetings = state.value.meetings.concatenate(nextMeetings)
                    )
                }.onFailure {
                    mutableState.value =
                        state.value.copy(
                            meetings = state.value.meetings.loading(false),
                            exception = it
                        )
                }
        }
    }

    fun addFriend(action: () -> Unit) {
        screenModelScope.launch {
            friendRepository.addFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    showDialog(
                        DialogRequest(
                            title = getString(
                                Res.string.friend_added,
                                state.value.stranger.nickname
                            ),
                            description = getString(
                                Res.string.friend_added_desc,
                                state.value.stranger.nickname
                            ),
                            actionTextRes = Res.string.create_meeting,
                            subActionTextRes = Res.string.close,
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
                            actionTextRes = Res.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun block() {
        val targetNickname = state.value.stranger.nickname
        screenModelScope.launch {
            showDialog(
                DialogRequest(
                    title = getString(Res.string.block_friend_dialog, targetNickname),
                    description = getString(Res.string.block_friend_dialog_desc, targetNickname),
                    actionTextRes = Res.string.block,
                    subActionTextRes = Res.string.cancel,
                    onAction = {
                        hideDialog()
                        onBlock()
                    },
                    onSubAction = ::hideDialog
                )
            )
        }
    }

    private fun onBlock() {
        screenModelScope.launch {
            friendRepository.blockFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    mutableState.value = state.value.copy(
                        stranger = state.value.stranger.copy(blocked = true)
                    )
                }
                .onFailure {
                    showDialog(
                        DialogRequest(
                            title = getString(Res.string.failed_to_block_friend),
                            description = getString(Res.string.failed_to_block_friend_desc),
                            actionTextRes = Res.string.confirm,
                            onAction = ::hideDialog
                        )
                    )
                }
        }
    }

    fun unblock() {
        val targetNickname = state.value.stranger.nickname
        screenModelScope.launch {
            showDialog(
                DialogRequest(
                    title = getString(Res.string.unblock_friend_dialog, targetNickname),
                    description = getString(Res.string.unblock_friend_dialog_desc, targetNickname),
                    actionTextRes = Res.string.unblock,
                    subActionTextRes = Res.string.cancel,
                    onAction = {
                        hideDialog()
                        onUnblock()
                    },
                    onSubAction = ::hideDialog
                )
            )
        }
    }

    fun onUnblock() {
        screenModelScope.launch {
            friendRepository.unblockFriend(targetId)
                .onSuccess {
                    refreshFriendScreen()
                    mutableState.value = state.value.copy(
                        stranger = state.value.stranger.copy(blocked = false)
                    )
                }
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

    private fun showDialog(request: DialogRequest) {
        mutableState.value = state.value.copy(dialogRequest = request)
    }

    private fun refreshFriendScreen() {
        friendScreenModel.refresh()
    }

    fun hideDialog() {
        mutableState.value = state.value.copy(dialogRequest = null)
    }

    fun clearException() {
        mutableState.value = state.value.copy(exception = null)
    }
}
