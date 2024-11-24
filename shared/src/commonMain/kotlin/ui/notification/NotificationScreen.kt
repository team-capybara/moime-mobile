package ui.notification

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
import ui.jsbridge.PopHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.main.MainScreenModel

class NotificationScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainScreenModel = koinScreenModel<MainScreenModel>()
        val popHandler = PopHandler {
            mainScreenModel.refreshUnreadNotification()
            navigator.pop()
        }

        BackHandler(true) {
            mainScreenModel.refreshUnreadNotification()
            navigator.pop()
        }

        MoimeWebView(
            url = WEBVIEW_BASE_URL + NotificationScreenModel.WEBVIEW_URL_PATH_NOTIFICATION,
            jsMessageHandlers = listOf(popHandler)
        )
    }
}
