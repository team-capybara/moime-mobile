package team.capybara.moime.ui.notification

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
import team.capybara.moime.ui.jsbridge.PopHandler
import team.capybara.moime.ui.jsbridge.WEBVIEW_BASE_URL
import team.capybara.moime.ui.main.MainScreenModel

class NotificationScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val mainScreenModel = scopeScreenModel<MainScreenModel>()
        val popHandler =
            PopHandler {
                mainScreenModel.refreshUnreadNotification()
                navigator.pop()
            }

        BackHandler(true) {
            mainScreenModel.refreshUnreadNotification()
            navigator.pop()
        }

        MoimeWebView(
            url = WEBVIEW_BASE_URL + NotificationScreenModel.WEBVIEW_URL_PATH_NOTIFICATION,
            jsMessageHandlers = listOf(popHandler),
        )
    }
}
