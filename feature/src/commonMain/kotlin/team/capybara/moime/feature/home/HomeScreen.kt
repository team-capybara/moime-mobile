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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.main.MainScreenModel

class HomeScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainScreenModel = scopeScreenModel<MainScreenModel>()
        val homeScreenModel = scopeScreenModel<HomeScreenModel>()
        val homeState by homeScreenModel.state.collectAsState()

        when (mainScreenModel.tabViewState.currentHomeTabView) {
            HomeTabView.ListView ->
                if (homeState.homeListState.isMeetingInitialized) {
                    HomeListView(
                        state = homeState.homeListState,
                        onRefresh = { homeScreenModel.refreshListState() },
                        onLoadCompletedMeetings = { homeScreenModel.loadCompleteMeetings() },
                        isActiveMeetingVisible = mainScreenModel.topAppBarBackgroundVisible.not(),
                        onActiveMeetingVisibleChanged = {
                            mainScreenModel.setTopAppBarBackgroundVisibility(it.not())
                        },
                    )
                } else {
                    MoimeLoading()
                }

            HomeTabView.CalendarView -> {
                HomeCalendarView(
                    state = homeState.homeCalendarState,
                    onDayClicked = { day ->
                        homeScreenModel.loadMeetingOfDay(day) {
                            mainScreenModel.showMeetingsBottomSheet(it)
                        }
                    },
                    onRefresh = { homeScreenModel.refreshCalendarState() },
                )
            }
        }
    }
}
