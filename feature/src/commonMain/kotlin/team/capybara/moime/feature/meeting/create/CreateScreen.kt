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

package team.capybara.moime.feature.meeting.create

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.FriendDetailNavigationHandler
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.friend.FriendDetailScreen
import team.capybara.moime.feature.home.HomeScreenModel

class CreateScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = scopeScreenModel<HomeScreenModel>()
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

        MoimeWebView(
            url = WEB_VIEW_BASE_URL + CreateScreenModel.WEB_VIEW_MEETING_CREATION_URL,
            jsMessageHandlers = listOf(popHandler, friendDetailNavigationHandler),
        )
    }
}
