package team.capybara.moime.feature.friend

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.cancel
import moime.core.designsystem.generated.resources.close
import moime.core.designsystem.generated.resources.confirm
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.block
import moime.feature.generated.resources.block_friend_dialog
import moime.feature.generated.resources.block_friend_dialog_desc
import moime.feature.generated.resources.create_meeting
import moime.feature.generated.resources.failed_to_add_friend
import moime.feature.generated.resources.failed_to_add_friend_desc
import moime.feature.generated.resources.failed_to_block_friend
import moime.feature.generated.resources.failed_to_block_friend_desc
import moime.feature.generated.resources.failed_to_unblock_friend
import moime.feature.generated.resources.failed_to_unblock_friend_desc
import moime.feature.generated.resources.friend_added
import moime.feature.generated.resources.friend_added_desc
import moime.feature.generated.resources.unblock
import moime.feature.generated.resources.unblock_friend_dialog
import moime.feature.generated.resources.unblock_friend_dialog_desc
import org.jetbrains.compose.resources.getString
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.data.repository.FriendRepository
import team.capybara.moime.core.data.repository.MeetingRepository
import team.capybara.moime.core.designsystem.component.DialogRequest
import team.capybara.moime.core.model.Friend
import team.capybara.moime.core.model.Meeting
import moime.core.designsystem.generated.resources.Res as MoimeRes

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

    private fun refresh() {
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
        if (state.value.meetings.canRequest().not() || mutableState.value.exception != null) return
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

    fun block() {
        val targetNickname = state.value.stranger.nickname
        screenModelScope.launch {
            showDialog(
                DialogRequest(
                    title = getString(Res.string.block_friend_dialog, targetNickname),
                    description = getString(Res.string.block_friend_dialog_desc, targetNickname),
                    actionTextRes = Res.string.block,
                    subActionTextRes = MoimeRes.string.cancel,
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
                            actionTextRes = MoimeRes.string.confirm,
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
                    subActionTextRes = MoimeRes.string.cancel,
                    onAction = {
                        hideDialog()
                        onUnblock()
                    },
                    onSubAction = ::hideDialog
                )
            )
        }
    }

    private fun onUnblock() {
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
