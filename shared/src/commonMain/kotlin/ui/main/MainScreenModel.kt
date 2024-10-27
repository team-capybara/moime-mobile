package ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import ui.model.Meeting
import ui.model.User
import ui.repository.NotificationRepository
import ui.repository.UserRepository

class MainScreenModel(
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : StateScreenModel<MainScreenModel.State>(State.Init) {

    var tabViewState by mutableStateOf(MainTabViewState())
        private set
    var topAppBarBackgroundVisible by mutableStateOf(true)
        private set
    var selectedDateMeetings by mutableStateOf<List<Meeting>?>(null)
        private set
    var hasUnreadNotification by mutableStateOf(false)
        private set

    sealed interface State {
        data object Init : State
        data object Loading : State
        data class Success(val user: User) : State
        data class Failure(val throwable: Throwable) : State
    }

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value = State.Init
        getUser()
        refreshUnreadNotification()
    }

    private fun getUser() {
        screenModelScope.launch {
            mutableState.value = State.Loading
            userRepository.getUser()
                .onSuccess { mutableState.value = State.Success(it) }
                .onFailure { mutableState.value = State.Failure(it) }
        }
    }

    fun setTopAppBarBackgroundVisibility(visible: Boolean) {
        topAppBarBackgroundVisible = visible
    }

    fun setCurrentTabView(tabView: MainTabView) {
        tabViewState = tabViewState.copy(tabView)
    }

    fun showMeetingsBottomSheet(meetings: List<Meeting>) {
        selectedDateMeetings = meetings
    }

    fun hideMeetingsBottomSheet() {
        selectedDateMeetings = null
    }

    fun refreshUnreadNotification() {
        screenModelScope.launch {
            notificationRepository.hasUnreadNotification()
                .onSuccess { hasUnreadNotification = it }
                .onFailure { mutableState.value = State.Failure(it) }
        }
    }
}
