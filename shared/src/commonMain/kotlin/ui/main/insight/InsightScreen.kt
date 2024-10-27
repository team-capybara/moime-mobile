package ui.main.insight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import ui.component.ExceptionDialog
import ui.component.MoimeLoading
import ui.main.MainScreenModel

class InsightScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val mainScreenModel = koinScreenModel<MainScreenModel>()
        val insightScreenModel = koinScreenModel<InsightScreenModel>()
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

                    InsightTabView.Friend -> {}
                }
            }

            is InsightScreenModel.State.Failure -> {
                ExceptionDialog(
                    exception = state.throwable,
                    onDismiss = { insightScreenModel.refresh() }
                )
            }
        }
    }
}
