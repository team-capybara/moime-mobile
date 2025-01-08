package team.capybara.moime.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import team.capybara.moime.core.designsystem.component.MoimeLoading
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.ui.jsbridge.AccessTokenHandler

@OptIn(InternalVoyagerApi::class)
@Composable
fun MoimeWebView(
    url: String,
    jsMessageHandlers: List<IJsMessageHandler> = emptyList(),
    onDispose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val webViewNavigator = rememberWebViewNavigator()
    val webViewState = rememberWebViewState(url)
    val jsBridge = rememberWebViewJsBridge(webViewNavigator)

    LaunchedEffect(jsBridge) {
        with(jsBridge) {
            jsMessageHandlers.forEach { register(it) }
            register(AccessTokenHandler())
        }
    }

    onDispose?.let {
        BackHandler(webViewNavigator.canGoBack.not(), it)
    }

    SafeAreaColumn {
        Box {
            WebView(
                state = webViewState.apply {
                    webSettings.backgroundColor = Gray700
                    webSettings.androidWebSettings.domStorageEnabled = true
                },
                navigator = webViewNavigator,
                webViewJsBridge = jsBridge,
                modifier = modifier.then(Modifier.fillMaxSize())
            )
            if (webViewState.isLoading) {
                MoimeLoading()
            }
        }
    }
}
