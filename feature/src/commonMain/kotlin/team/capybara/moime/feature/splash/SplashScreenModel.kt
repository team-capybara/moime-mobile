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

package team.capybara.moime.feature.splash

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.cookie.Cookie
import com.multiplatform.webview.cookie.WebViewCookieManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.capybara.moime.core.common.di.ScopeProvider
import team.capybara.moime.core.common.model.BearerTokenStorage
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_COOKIE_DOMAIN

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
                url = WEB_VIEW_BASE_URL,
                cookie = Cookie(
                    name = "Authorization",
                    value = "Bearer $token",
                    domain = WEB_VIEW_COOKIE_DOMAIN
                )
            )
        }
    }

    companion object {
        private const val SPLASH_DELAY_MILLIS = 1000L
    }
}
