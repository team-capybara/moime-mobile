package ui.login

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
import ui.component.ExceptionDialog
import ui.component.MoimeImagePicker
import ui.component.MoimeWebView
import ui.jsbridge.ImageStringData
import ui.main.MainScreen
import ui.onboarding.OnboardingScreen
import ui.util.Base64Util.encodeToBase64

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
            url = LoginScreenModel.WEBVIEW_LOGIN_URL,
            accessToken = null,
            jsMessageHandlers = listOf(
                screenModel.LoginJsMessageHandler(),
                screenModel.imagePickerHandler
            )
        )

        (loginState as? LoginScreenModel.State.InProgress)?.onImagePicked?.let { callback ->
            MoimeImagePicker(onPicked = { images ->
                images.firstOrNull()?.let {
                    callback(Json.encodeToString(ImageStringData(it.encodeToBase64())))
                }
            })
        }

        (loginState as? LoginScreenModel.State.Failure)?.throwable?.let {
            ExceptionDialog(
                exception = it,
                onDismiss = { navigator.pop() }
            )
        }
    }
}
