package team.capybara.moime.ui.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class PopHandler(
    private val onPop: () -> Unit
) : IJsMessageHandler {

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        onPop()
    }

    override fun methodName(): String = METHOD_NAME

    companion object {
        private const val METHOD_NAME = "onPop"
    }
}
