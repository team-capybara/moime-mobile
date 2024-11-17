package ui.meeting.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import com.russhwolf.settings.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ui.component.MoimeWebView
import ui.friend.FriendDetailScreen
import ui.jsbridge.ACCESS_TOKEN_KEY
import ui.jsbridge.FriendDetailNavigationHandler
import ui.jsbridge.PopHandler
import ui.main.home.HomeScreenModel
import ui.meeting.camera.CameraScreen
import ui.model.Meeting

data class MeetingScreen(private val meeting: Meeting) : Screen, KoinComponent {

    private val settings: Settings by inject()
    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = koinScreenModel<HomeScreenModel>()
        val meetingScreenModel = rememberScreenModel { MeetingScreenModel(meeting) }
        val state by meetingScreenModel.state.collectAsState()
        val popHandler = PopHandler {
            navigator.pop()
            homeScreenModel.refresh()
        }
        val friendDetailNavigationHandler = FriendDetailNavigationHandler {
            navigator.push(FriendDetailScreen(it))
        }

        BackHandler(true) {
            navigator.pop()
            homeScreenModel.refresh()
        }

        LaunchedEffect(state) {
            if (state is MeetingScreenModel.State.NavigateToCamera) {
                navigator.push(CameraScreen(meeting.id))
                meetingScreenModel.initState()
            }
        }

        MoimeWebView(
            url = meetingScreenModel.webViewUrl,
            accessToken = settings.getString(ACCESS_TOKEN_KEY, ""),
            jsMessageHandlers = listOf(
                meetingScreenModel.CameraJsMessageHandler(),
                meetingScreenModel.imageDownloadHandler,
                popHandler,
                friendDetailNavigationHandler
            )
        )
    }
}
