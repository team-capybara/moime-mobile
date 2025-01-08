package team.capybara.moime.feature.login

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.mmk.kmpnotifier.notification.NotifierManager
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.dataToJsonString
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.coroutines.launch
import team.capybara.moime.core.common.di.ScopeProvider
import team.capybara.moime.core.data.repository.UserRepository
import team.capybara.moime.core.ui.jsbridge.ImagePickerHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_COOKIE_DOMAIN

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
                        .onSuccess {
                            ScopeProvider.closeScope()
                            ScopeProvider.createScope()

                            setWebViewCookieManager(accessToken)

                            NotifierManager.getPushNotifier().getToken()?.let {
                                callback(dataToJsonString(LoginJsCallback(it)))
                            }

                            mutableState.value = State.Success(isNewbie = loginJsMessage.isNewbie)
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
            url = WEB_VIEW_BASE_URL,
            cookie = Cookie(
                name = "Authorization",
                value = "Bearer $token",
                domain = WEB_VIEW_COOKIE_DOMAIN
            )
        )
    }

    fun reset() {
        mutableState.value = State.InProgress()
    }

    companion object {
        const val WEB_VIEW_LOGIN_URL = "https://www.moime.app/login"
        private const val BRIDGE_LOGIN_METHOD_NAME = "onLoginSuccess"
    }
}
