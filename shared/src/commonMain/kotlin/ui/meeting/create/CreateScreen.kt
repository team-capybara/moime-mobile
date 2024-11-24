package ui.meeting.create

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import ui.component.MoimeWebView
import ui.friend.FriendDetailScreen
import ui.jsbridge.FriendDetailNavigationHandler
import ui.jsbridge.PopHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.main.home.HomeScreenModel

class CreateScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = koinScreenModel<HomeScreenModel>()
        val popHandler = PopHandler {
            navigator.pop()
            homeScreenModel.refresh()
        }
        val friendDetailNavigationHandler = FriendDetailNavigationHandler {
            navigator.push(FriendDetailScreen(it))
        }

        BackHandler(true) {
            navigator.pop()
            homeScreenModel.refresh()
        }

        MoimeWebView(
            url = WEBVIEW_BASE_URL + CreateScreenModel.WEBVIEW_MEETING_CREATION_URL,
            jsMessageHandlers = listOf(popHandler, friendDetailNavigationHandler)
        )
    }
}
