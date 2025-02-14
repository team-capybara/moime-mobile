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

package team.capybara.moime.feature.meeting.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import team.capybara.moime.core.model.Meeting
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.FriendDetailNavigationHandler
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.friend.FriendDetailScreen
import team.capybara.moime.feature.home.HomeScreenModel
import team.capybara.moime.feature.meeting.camera.CameraScreen

data class MeetingScreen(
    private val meeting: Meeting,
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = scopeScreenModel<HomeScreenModel>()
        val meetingScreenModel = rememberScreenModel { MeetingScreenModel(meeting) }
        val state by meetingScreenModel.state.collectAsState()
        val popHandler =
            PopHandler {
                navigator.pop()
                homeScreenModel.refresh()
            }
        val friendDetailNavigationHandler =
            FriendDetailNavigationHandler {
                navigator.push(FriendDetailScreen(it))
            }

        BackHandler(true) {
            navigator.pop()
            homeScreenModel.refresh()
        }

        LaunchedEffect(state) {
            if (state is MeetingScreenModel.State.NavigateToCamera) {
                navigator.push(CameraScreen(meeting.id))
                meetingScreenModel.initState()
            }
        }

        MoimeWebView(
            url = meetingScreenModel.webViewUrl,
            jsMessageHandlers =
                listOf(
                    meetingScreenModel.CameraJsMessageHandler(),
                    meetingScreenModel.imageDownloadHandler,
                    popHandler,
                    friendDetailNavigationHandler,
                ),
        )
    }
}
