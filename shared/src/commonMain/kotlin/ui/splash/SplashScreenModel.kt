package ui.splash

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import di.BearerTokenStorage
import di.ScopeProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.jsbridge.COOKIE_DOMAIN
import ui.jsbridge.WEBVIEW_BASE_URL

class SplashScreenModel(
    private val bearerTokenStorage: BearerTokenStorage,
) : StateScreenModel<SplashScreenModel.State>(State.Init) {

    sealed interface State {
        data object Init : State
        data object Authorized : State
        data object Unauthorized : State
    }

    init {
        screenModelScope.launch {
            delay(SPLASH_DELAY_MILLIS)
            bearerTokenStorage.lastOrNull()?.let {
                ScopeProvider.createScope()
                setWebViewCookieManager(it.accessToken)
                mutableState.value = State.Authorized
            } ?: run {
                mutableState.value = State.Unauthorized
            }
        }
    }

    private fun setWebViewCookieManager(token: String) {
        screenModelScope.launch {
            WebViewCookieManager().setCookie(
                url = WEBVIEW_BASE_URL,
                cookie = Cookie(
                    name = "Authorization",
                    value = "Bearer $token",
                    domain = COOKIE_DOMAIN
                )
            )
        }
    }

    companion object {
        private const val SPLASH_DELAY_MILLIS = 1000L
    }
}
