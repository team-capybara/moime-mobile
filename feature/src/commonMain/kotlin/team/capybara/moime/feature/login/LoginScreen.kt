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
