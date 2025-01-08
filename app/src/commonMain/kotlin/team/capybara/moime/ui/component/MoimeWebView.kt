package team.capybara.moime.ui.component

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
import team.capybara.moime.ui.jsbridge.AccessTokenHandler
import team.capybara.moime.ui.theme.Gray700

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
