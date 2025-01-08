package team.capybara.moime.ui.meeting.create

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import team.capybara.moime.di.ScopeProvider.scopeScreenModel
import team.capybara.moime.ui.component.MoimeWebView
import team.capybara.moime.ui.friend.FriendDetailScreen
import team.capybara.moime.ui.jsbridge.FriendDetailNavigationHandler
import team.capybara.moime.ui.jsbridge.PopHandler
import team.capybara.moime.ui.jsbridge.WEBVIEW_BASE_URL
import team.capybara.moime.ui.main.home.HomeScreenModel

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
            url = WEBVIEW_BASE_URL + CreateScreenModel.WEBVIEW_MEETING_CREATION_URL,
            jsMessageHandlers = listOf(popHandler, friendDetailNavigationHandler),
        )
    }
}
