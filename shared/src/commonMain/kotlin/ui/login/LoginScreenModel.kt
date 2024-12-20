package ui.login

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import di.ScopeProvider
import kotlinx.coroutines.launch
import ui.jsbridge.COOKIE_DOMAIN
import ui.jsbridge.ImagePickerHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.repository.UserRepository

class LoginScreenModel(
    private val userRepository: UserRepository
) : StateScreenModel<LoginScreenModel.State>(State.InProgress()) {

    sealed interface State {
        data class InProgress(val onImagePicked: ((String) -> Unit)? = null) : State
        data class Success(val isNewbie: Boolean) : State
        data class Failure(val throwable: Throwable?) : State
    }

    val imagePickerHandler = ImagePickerHandler { callback ->
        if (state.value is State.InProgress) {
            mutableState.value = State.InProgress(onImagePicked = callback)
        }
    }

    inner class LoginJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            val loginJsMessage = processParams<LoginJsMessage>(message)
            loginJsMessage.accessToken.takeIf { it.isNotBlank() }?.let { accessToken ->
                screenModelScope.launch {
                    userRepository.login(accessToken)
                        .onSuccess { fcmToken ->
                            ScopeProvider.closeScope()
                            ScopeProvider.createScope()
                            setWebViewCookieManager(accessToken)
                            mutableState.value = State.Success(isNewbie = loginJsMessage.isNewbie)
                            fcmToken?.let { callback(dataToJsonString(LoginJsCallback(it))) }
                        }
                        .onFailure { State.Failure(it) }
                }
            } ?: run {
                mutableState.value = State.Failure(Throwable("AccessToken is blank."))
            }
        }

        override fun methodName(): String = BRIDGE_LOGIN_METHOD_NAME
    }

    private suspend fun setWebViewCookieManager(token: String) {
        WebViewCookieManager().setCookie(
            url = WEBVIEW_BASE_URL,
            cookie = Cookie(
                name = "Authorization",
                value = "Bearer $token",
                domain = COOKIE_DOMAIN
            )
        )
    }

    fun reset() {
        mutableState.value = State.InProgress()
    }

    companion object {
        const val WEBVIEW_LOGIN_URL = "https://www.moime.app/login"
        private const val BRIDGE_LOGIN_METHOD_NAME = "onLoginSuccess"
    }
}
