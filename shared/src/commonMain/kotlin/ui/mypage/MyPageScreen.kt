package ui.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.russhwolf.settings.Settings
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.component.MoimeImagePicker
import ui.component.MoimeWebView
import ui.jsbridge.ACCESS_TOKEN_KEY
import ui.jsbridge.ImagePickerResponse
import ui.jsbridge.PopHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.login.LoginScreen
import ui.util.Base64Util.encodeToBase64

class MyPageScreen : Screen, KoinComponent {

    private val settings: Settings by inject()
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val popHandler = PopHandler { navigator.pop() }

        val permissionFactory = rememberPermissionsControllerFactory()
        val permissionController = remember(permissionFactory) {
            permissionFactory.createPermissionsController()
        }
        BindEffect(permissionController)

        val screenModel = rememberScreenModel { MyPageScreenModel(permissionController) }
        val state by screenModel.state.collectAsState()

        LaunchedEffect(state.logoutRequired) {
            if (state.logoutRequired) {
                navigator.replaceAll(LoginScreen())
            }
        }

        MoimeWebView(
            url = WEBVIEW_BASE_URL + MyPageScreenModel.WEBVIEW_URL_PATH_MY_PAGE,
            accessToken = settings.getString(ACCESS_TOKEN_KEY, ""),
            jsMessageHandlers = screenModel.jsMessageHandler.getHandlers() + listOf(
                screenModel.imagePickerHandler,
                popHandler
            )
        )

        state.onImagePicked?.let { callback ->
            MoimeImagePicker(onPicked = { images ->
                images.firstOrNull()?.let {
                    callback(Json.encodeToString(ImagePickerResponse(it.encodeToBase64())))
                }
            })
        }
    }
}
