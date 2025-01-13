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
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import moime.core.designsystem.generated.resources.ic_home
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.feature.main.MainTab
import team.capybara.moime.feature.main.MainTabView
import moime.core.designsystem.generated.resources.Res as MoimeRes

object HomeTab : MainTab {

    override val tabViews: List<MainTabView> = HomeTabView.entries

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(MoimeRes.drawable.ic_home)

            return remember {
                TabOptions(
                    index = 0u,
                    title = "Home",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        HomeScreen().Content()
    }
}
