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
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.home_tab_calendar_view
import moime.feature.generated.resources.home_tab_list_view
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.feature.main.MainTabView

enum class HomeTabView(
    override val titleTextRes: StringResource
) : MainTabView {
    ListView(
        titleTextRes = Res.string.home_tab_list_view
    ),
    CalendarView(
        titleTextRes = Res.string.home_tab_calendar_view
    );

    @Composable
    override fun getTitleText(): String = stringResource(titleTextRes)
}
