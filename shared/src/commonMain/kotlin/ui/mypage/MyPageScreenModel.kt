package ui.mypage

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.cookie.WebViewCookieManager
import com.russhwolf.settings.Settings
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.jsbridge.ACCESS_TOKEN_KEY
import ui.jsbridge.APP_VERSION
import ui.jsbridge.ImagePickerHandler
import ui.login.LoginScreenModel

class MyPageScreenModel(
    private val permissionsController: PermissionsController
) : StateScreenModel<MyPageScreenModel.State>(State()), KoinComponent {

    private val settings: Settings by inject()
    private val loginScreenModel: LoginScreenModel by inject()

    data class State(
        val onImagePicked: ((String) -> Unit)? = null,
        val logoutRequested: Boolean = false
    )

    val jsMessageHandler = MyPageJsMessageHandler(
        onGetNotificationPermission = ::onGetNotificationPermission,
        onGetAppVersion = ::onGetAppVersion,
        onLogout = ::onLogout
    )

    val imagePickerHandler = ImagePickerHandler { callback ->
        mutableState.value = state.value.copy(onImagePicked = callback)
    }

    private fun onGetNotificationPermission(callback: (String) -> Unit) {
        screenModelScope.launch {
            val jsCallbackResponse = PermissionJsCallback(
                granted = permissionsController.isPermissionGranted(Permission.REMOTE_NOTIFICATION)
            )
            callback(Json.encodeToString((jsCallbackResponse)))
        }
    }

    private fun onGetAppVersion(callback: (String) -> Unit) {
        val jsCallbackResponse = AppVersionJsCallback(
            version = APP_VERSION
        )
        callback(Json.encodeToString((jsCallbackResponse)))
    }

    private fun onLogout() {
        screenModelScope.launch {
            settings.remove(ACCESS_TOKEN_KEY)
            WebViewCookieManager().removeAllCookies()
            loginScreenModel.reset()
            mutableState.value = state.value.copy(logoutRequested = true)
        }
    }

    companion object {
        internal const val WEBVIEW_URL_PATH_MY_PAGE = "mypage"
    }
}
