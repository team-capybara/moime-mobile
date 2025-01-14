/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package team.capybara.moime.feature.mypage

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.cookie.WebViewCookieManager
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import team.capybara.moime.core.data.repository.UserRepository
import team.capybara.moime.core.ui.jsbridge.APP_VERSION
import team.capybara.moime.core.ui.jsbridge.ImagePickerHandler
import team.capybara.moime.feature.login.LoginScreenModel

class MyPageScreenModel(
    private val permissionsController: PermissionsController
) : StateScreenModel<MyPageScreenModel.State>(State()), KoinComponent {

    private val loginScreenModel: LoginScreenModel by inject()
    private val userRepository: UserRepository by inject()

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
            userRepository.logout()
            WebViewCookieManager().removeAllCookies()
            loginScreenModel.reset()
            mutableState.value = state.value.copy(logoutRequested = true)
        }
    }

    companion object {
        internal const val WEB_VIEW_URL_PATH_MY_PAGE = "mypage"
    }
}
