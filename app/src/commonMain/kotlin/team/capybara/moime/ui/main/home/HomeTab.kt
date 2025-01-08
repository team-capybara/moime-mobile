package team.capybara.moime.ui.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import moime.app.generated.resources.Res
import moime.app.generated.resources.ic_home
import org.jetbrains.compose.resources.painterResource
import team.capybara.moime.ui.main.MainTab
import team.capybara.moime.ui.main.MainTabView

object HomeTab : MainTab {

    override val tabViews: List<MainTabView> = HomeTabView.entries

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_home)

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
