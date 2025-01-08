package team.capybara.moime.feature.meeting.create

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.FriendDetailNavigationHandler
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.friend.FriendDetailScreen
import team.capybara.moime.feature.home.HomeScreenModel

class CreateScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = scopeScreenModel<HomeScreenModel>()
        val popHandler =
            PopHandler {
                navigator.pop()
                homeScreenModel.refresh()
            }
        val friendDetailNavigationHandler =
            FriendDetailNavigationHandler {
                navigator.push(FriendDetailScreen(it))
            }

        BackHandler(true) {
            navigator.pop()
            homeScreenModel.refresh()
        }

        MoimeWebView(
            url = WEB_VIEW_BASE_URL + CreateScreenModel.WEB_VIEW_MEETING_CREATION_URL,
            jsMessageHandlers = listOf(popHandler, friendDetailNavigationHandler),
        )
    }
}
