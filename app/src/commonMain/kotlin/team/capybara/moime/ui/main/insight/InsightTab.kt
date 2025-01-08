package team.capybara.moime.ui.main.insight

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import moime.app.generated.resources.Res
import moime.app.generated.resources.ic_clipboard_text
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.ui.main.MainTab
import team.capybara.moime.ui.main.MainTabView

object InsightTab : MainTab {

    override val tabViews: List<MainTabView> = InsightTabView.entries

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_clipboard_text)

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
