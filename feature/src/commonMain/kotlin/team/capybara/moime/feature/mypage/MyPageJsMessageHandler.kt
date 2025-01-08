package team.capybara.moime.feature.mypage

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class MyPageJsMessageHandler(
    private val onGetNotificationPermission: ((String) -> Unit) -> Unit,
    private val onGetAppVersion: ((String) -> Unit) -> Unit,
    private val onLogout: () -> Unit
) {

    inner class PermissionJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onGetNotificationPermission(callback)
        }

        override fun methodName(): String = "onGetNotificationPermission"
    }

    inner class AppVersionJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onGetAppVersion(callback)
        }

        override fun methodName(): String = "onGetAppVersion"
    }

    inner class LogoutJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            onLogout()
        }

        override fun methodName(): String = "onLogout"
    }

    fun getHandlers() = listOf(
        PermissionJsMessageHandler(),
        AppVersionJsMessageHandler(),
        LogoutJsMessageHandler()
    )
}
