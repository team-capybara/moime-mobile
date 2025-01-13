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

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.model.User
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.component.MeetingsBottomSheet
import team.capybara.moime.feature.component.MoimeBottomNavigationBar
import team.capybara.moime.feature.component.MoimeMainTopAppBar
import team.capybara.moime.feature.friend.FriendScreen
import team.capybara.moime.feature.home.HomeTab
import team.capybara.moime.feature.insight.InsightTab
import team.capybara.moime.feature.meeting.create.CreateScreen
import team.capybara.moime.feature.meeting.detail.MeetingScreen
import team.capybara.moime.feature.mypage.MyPageScreen
import team.capybara.moime.feature.notification.NotificationScreen

class MainScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainScreenModel = scopeScreenModel<MainScreenModel>()
        val mainState by mainScreenModel.state.collectAsState()
        var user by remember { mutableStateOf<User?>(null) }
        val selectedDateMeetings = mainScreenModel.selectedDateMeetings

        LaunchedEffect(mainState) {
            val state = mainState
            if (state is MainScreenModel.State.Success) {
                user = state.user
            }
        }

        TabNavigator(
            tab = HomeTab,
            tabDisposable = {
                TabDisposable(
                    navigator = it,
                    tabs = listOf(HomeTab, InsightTab),
                )
            },
        ) { tabNavigator ->
            Scaffold(
                topBar = {
                    val currentTabNavigator = tabNavigator.current as MainTab
                    MoimeMainTopAppBar(
                        profileImageUrl = user?.profileImageUrl ?: "",
                        currentTab = currentTabNavigator,
                        currentTabView =
                            mainScreenModel.tabViewState.getCurrentTabViewWithTab(
                                currentTabNavigator,
                            ),
                        onClickProfile = { navigator.push(MyPageScreen()) },
                        onClickUserAdd = {
                            navigator.push(FriendScreen(user))
                        },
                        onClickNotification = {
                            navigator.push(NotificationScreen())
                        },
                        onTabViewChanged = {
                            mainScreenModel.setCurrentTabView(it)
                        },
                        hasUnreadNotification = mainScreenModel.hasUnreadNotification,
                        hiddenBackground = mainScreenModel.topAppBarBackgroundVisible.not(),
                    )
                },
                content = {
                    when (mainState) {
                        MainScreenModel.State.Init -> {}
                        MainScreenModel.State.Loading -> {
                            MoimeLoading()
                        }

                        is MainScreenModel.State.Success -> {
                            Box {
                                CurrentTab()
                                selectedDateMeetings?.let { meetings ->
                                    MeetingsBottomSheet(
                                        meetings = meetings,
                                        onClickMeeting = { navigator.push(MeetingScreen(it)) },
                                        onDismissRequest = { mainScreenModel.hideMeetingsBottomSheet() },
                                    )
                                }
                            }
                        }

                        is MainScreenModel.State.Failure -> {

                        }
                    }
                },
                bottomBar = {
                    MoimeBottomNavigationBar(
                        onAction = { navigator.push(CreateScreen()) },
                    )
                },
            )
        }
    }
}
