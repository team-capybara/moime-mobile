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

package team.capybara.moime.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.capybara.moime.core.common.util.Base64Util.encodeToBase64
import team.capybara.moime.core.ui.component.MoimeImagePicker
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.ImageStringData
import team.capybara.moime.feature.main.MainScreen
import team.capybara.moime.feature.onboarding.OnboardingScreen

class LoginScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = koinScreenModel<LoginScreenModel>()
        val loginState by screenModel.state.collectAsState()

        LaunchedEffect(loginState) {
            val state = loginState
            if (state is LoginScreenModel.State.Success) {
                navigator.replace(if (state.isNewbie) OnboardingScreen() else MainScreen())
            }
        }

        MoimeWebView(
            url = LoginScreenModel.WEB_VIEW_LOGIN_URL,
            jsMessageHandlers = listOf(
                screenModel.LoginJsMessageHandler(),
                screenModel.imagePickerHandler
            )
        )

        (loginState as? LoginScreenModel.State.InProgress)?.onImagePicked?.let { callback ->
            MoimeImagePicker(onPicked = {
                callback(Json.encodeToString(ImageStringData(it.encodeToBase64())))
            })
        }
    }
}
