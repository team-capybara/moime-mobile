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
