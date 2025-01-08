package team.capybara.moime.feature.notification

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.main.MainScreenModel

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
            url = WEB_VIEW_BASE_URL + NotificationScreenModel.WEB_VIEW_URL_PATH_NOTIFICATION,
            jsMessageHandlers = listOf(popHandler),
        )
    }
}
