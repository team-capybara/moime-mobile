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

package team.capybara.moime.feature.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.model.User
import team.capybara.moime.core.data.repository.NotificationRepository
import team.capybara.moime.core.data.repository.UserRepository

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
        }
    }

    fun clearException() {
        mutableState.value = State.Init
    }
}
