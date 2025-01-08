package team.capybara.moime.feature.insight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import moime.core.designsystem.generated.resources.ic_clipboard_text
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.feature.main.MainTab
import team.capybara.moime.feature.main.MainTabView
import moime.core.designsystem.generated.resources.Res as MoimeRes

object InsightTab : MainTab {

    override val tabViews: List<MainTabView> = InsightTabView.entries

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(MoimeRes.drawable.ic_clipboard_text)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Statistics",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        InsightScreen().Content()
    }
}
