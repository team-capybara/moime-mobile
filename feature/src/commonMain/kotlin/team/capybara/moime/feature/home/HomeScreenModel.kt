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

package team.capybara.moime.feature.home

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import team.capybara.moime.core.data.repository.MeetingRepository
import team.capybara.moime.core.model.Meeting

class HomeScreenModel(
    private val meetingRepository: MeetingRepository
) : StateScreenModel<HomeState>(HomeState()) {

    init {
        refresh()
    }

    fun refresh() {
        mutableState.value = HomeState()
        refreshListState()
        refreshCalendarState()
    }

    fun loadCompleteMeetings() {
        if (state.value.homeListState.completedMeetings.canRequest().not()) return
        screenModelScope.launch {
            mutableState.value = state.value.loadListCompletedMeetings()
            meetingRepository.getCompletedMeetings(state.value.homeListState.completedMeetings.nextRequest())
                .onSuccess {
                    mutableState.value = state.value.updateCompletedMeetings(it)
                    mutableState.value = state.value.listStateLoading(false)
                }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    private fun getOngoingMeetings() {
        screenModelScope.launch {
            meetingRepository.getAllOngoingMeetings()
                .onSuccess { mutableState.value = state.value.setOngoingMeetings(it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    private fun getUpcomingMeetings() {
        screenModelScope.launch {
            meetingRepository.getAllUpcomingMeetings()
                .onSuccess { mutableState.value = state.value.setUpcomingMeetings(it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    fun refreshListState() {
        mutableState.value = state.value.copy(homeListState = HomeListState())
        getUpcomingMeetings()
        getOngoingMeetings()
        loadCompleteMeetings()
    }

    fun refreshCalendarState() {
        mutableState.value = state.value.copy(homeCalendarState = HomeCalendarState())
        getMeetingCount()
    }

    private fun getMeetingCount(
        from: LocalDate = state.value.homeCalendarState.minDate,
        to: LocalDate = state.value.homeCalendarState.maxDate
    ) {
        screenModelScope.launch {
            meetingRepository.getMeetingsCount(from, to)
                .onSuccess { mutableState.value = state.value.setMeetingCount(it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    fun loadMeetingOfDay(date: LocalDate, callback: (List<Meeting>) -> Unit) {
        screenModelScope.launch {
            meetingRepository.getMeetingsOfDay(date)
                .onSuccess { callback(it) }
                .onFailure { mutableState.value = state.value.copy(exception = it) }
        }
    }

    fun clearException() {
        mutableState.value = state.value.copy(exception = null)
    }
}
