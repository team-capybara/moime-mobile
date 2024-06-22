package ui.main.tab

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.TabOptions
import team.capybara.moime.SharedRes
import ui.main.home.HomeScreen

data object HomeTab : MainTab {
    override val iconResource = SharedRes.images.ic_home

    override val options: TabOptions
        @Composable
        get() = remember {
            TabOptions(
                index = 0u,
                title = "Home"
            )
        }

    @Composable
    override fun Content() {
        HomeScreen().Content()
    }
}
