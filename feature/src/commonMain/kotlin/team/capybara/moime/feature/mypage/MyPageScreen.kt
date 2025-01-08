package team.capybara.moime.feature.mypage

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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import team.capybara.moime.core.common.util.Base64Util.encodeToBase64
import team.capybara.moime.core.ui.component.MoimeImagePicker
import team.capybara.moime.core.ui.component.MoimeWebView
import team.capybara.moime.core.ui.jsbridge.ImageStringData
import team.capybara.moime.core.ui.jsbridge.PopHandler
import team.capybara.moime.core.ui.jsbridge.WEB_VIEW_BASE_URL
import team.capybara.moime.core.ui.util.CoilUtil
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.login.LoginScreen
import team.capybara.moime.feature.main.MainScreenModel

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
            url = WEB_VIEW_BASE_URL + MyPageScreenModel.WEB_VIEW_URL_PATH_MY_PAGE,
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
