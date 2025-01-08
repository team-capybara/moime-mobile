package team.capybara.moime.core.ui.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.serialization.Serializable

class FriendDetailNavigationHandler(
    private val onNavigate: (Long) -> Unit
): IJsMessageHandler {

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        val friendId = processParams<FriendIdJsMessage>(message).friendId
        onNavigate(friendId)
    }

    override fun methodName(): String = METHOD_NAME

    companion object {
        private const val METHOD_NAME = "onNavigateToFriendDetail"
    }
}

@Serializable
data class FriendIdJsMessage(
    val friendId: Long
)
