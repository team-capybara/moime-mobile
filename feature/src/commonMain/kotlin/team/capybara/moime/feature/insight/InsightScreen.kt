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

package team.capybara.moime.feature.insight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.main.MainScreenModel

class InsightScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainScreenModel = scopeScreenModel<MainScreenModel>()
        val insightScreenModel = scopeScreenModel<InsightScreenModel>()
        val insightState by insightScreenModel.state.collectAsState()

        when (val state = insightState) {
            InsightScreenModel.State.Init -> {}

            InsightScreenModel.State.Loading -> {
                MoimeLoading()
            }

            is InsightScreenModel.State.Success -> {
                when (mainScreenModel.tabViewState.currentInsightTabView) {
                    InsightTabView.Summary -> {
                        InsightSummaryContent(state.summary)
                    }

                    InsightTabView.Friend -> {
                        InsightFriendContent(
                            survey = state.survey,
                            onSubmit = insightScreenModel::postSurvey,
                        )
                    }
                }
            }

            is InsightScreenModel.State.Failure -> {

            }
        }
    }
}
