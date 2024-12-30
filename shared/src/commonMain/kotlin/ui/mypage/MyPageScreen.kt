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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import coil3.compose.LocalPlatformContext
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import di.ScopeProvider.scopeScreenModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.component.MoimeImagePicker
import ui.component.MoimeWebView
import ui.jsbridge.ImageStringData
import ui.jsbridge.PopHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.login.LoginScreen
import ui.main.MainScreenModel
import ui.util.Base64Util.encodeToBase64
import ui.util.CoilUtil

class MyPageScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val permissionFactory = rememberPermissionsControllerFactory()
        val permissionController =
            remember(permissionFactory) {
                permissionFactory.createPermissionsController()
            }
        BindEffect(permissionController)

        val mainScreenModel = scopeScreenModel<MainScreenModel>()
        val myPageScreenModel = rememberScreenModel { MyPageScreenModel(permissionController) }
        val state by myPageScreenModel.state.collectAsState()
        val context = LocalPlatformContext.current
        val popHandler =
            PopHandler {
                CoilUtil.clearDiskCache(context)
                CoilUtil.clearMemoryCache(context)
                navigator.pop()
                mainScreenModel.refresh()
            }

        BackHandler(true) {
            CoilUtil.clearDiskCache(context)
            CoilUtil.clearMemoryCache(context)
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
            jsMessageHandlers =
                myPageScreenModel.jsMessageHandler.getHandlers() +
                    listOf(
                        myPageScreenModel.imagePickerHandler,
                        popHandler,
                    ),
        )

        state.onImagePicked?.let { callback ->
            MoimeImagePicker(onPicked = {
                callback(Json.encodeToString(ImageStringData(it.encodeToBase64())))
            })
        }
    }
}
