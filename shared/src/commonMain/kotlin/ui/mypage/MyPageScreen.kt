package ui.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.component.MoimeImagePicker
import ui.component.MoimeWebView
import ui.jsbridge.PopHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.login.LoginScreen
import ui.main.MainScreenModel
import ui.util.Base64Util.encodeToBase64
import ui.util.ResizeOptions
import ui.util.resize

class MyPageScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val permissionFactory = rememberPermissionsControllerFactory()
        val permissionController = remember(permissionFactory) {
            permissionFactory.createPermissionsController()
        }
        BindEffect(permissionController)

        val mainScreenModel = koinScreenModel<MainScreenModel>()
        val myPageScreenModel = rememberScreenModel { MyPageScreenModel(permissionController) }
        val state by myPageScreenModel.state.collectAsState()
        val popHandler = PopHandler {
            navigator.pop()
            mainScreenModel.refresh()
        }

        BackHandler(true) {
            navigator.pop()
            mainScreenModel.refresh()
        }

        LaunchedEffect(state.logoutRequested) {
            if (state.logoutRequested) {
                navigator.replaceAll(LoginScreen())
            }
        }

        MoimeWebView(
            url = WEBVIEW_BASE_URL + MyPageScreenModel.WEBVIEW_URL_PATH_MY_PAGE,
            jsMessageHandlers = myPageScreenModel.jsMessageHandler.getHandlers() + listOf(
                myPageScreenModel.imagePickerHandler,
                popHandler
            )
        )

        state.onImagePicked?.let { callback ->
            MoimeImagePicker(onPicked = { images ->
                images.firstOrNull()?.let {
                    val imageString = it.resize(ResizeOptions(256, 256)).encodeToBase64()
                    callback(Json.encodeToString(imageString))
                }
            })
        }
    }
}
