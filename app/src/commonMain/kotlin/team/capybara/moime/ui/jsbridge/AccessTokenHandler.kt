package team.capybara.moime.ui.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.web.WebViewNavigator
import team.capybara.moime.di.BearerTokenStorage
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AccessTokenHandler : IJsMessageHandler, KoinComponent {

    private val bearerTokenStorage: BearerTokenStorage by inject()

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        bearerTokenStorage.lastOrNull()?.accessToken?.let { token ->
            callback(dataToJsonString(AccessTokenCallback(token)))
        }
    }

    override fun methodName(): String = METHOD_NAME

    companion object {
        private const val METHOD_NAME = "getAccessToken"
    }
}

@Serializable
data class AccessTokenCallback(
    val accessToken: String
)
