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
