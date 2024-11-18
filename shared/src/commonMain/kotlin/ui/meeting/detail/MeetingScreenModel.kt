package ui.meeting.detail

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import ui.jsbridge.ImageDownloadHandler
import ui.jsbridge.WEBVIEW_BASE_URL
import ui.model.Meeting
import ui.util.DateUtil.now
import ui.util.ResizeOptions
import ui.util.resize

class MeetingScreenModel(meeting: Meeting) :
    StateScreenModel<MeetingScreenModel.State>(State.Init) {

    sealed interface State {
        data object Init : State
        data object NavigateToCamera : State
    }

    val webViewUrl = WEBVIEW_BASE_URL + when (meeting.status) {
        Meeting.Status.Created -> CREATED_MEETING_URL
        Meeting.Status.Ongoing -> ONGOING_MEETING_URL
        Meeting.Status.Finished -> FINISHED_MEETING_URL
        Meeting.Status.Completed -> COMPLETED_MEETING_URL
        else -> ERROR_URL
    } + "?$MEETING_ID_KEY=${meeting.id}"

    val imageDownloadHandler = ImageDownloadHandler(
        methodName = BRIDGE_DOWNLOAD_IMAGE_METHOD_NAME,
        onDownload = {
            screenModelScope.launch {
                FileKit.saveFile(
                    baseName = "moime-${LocalDateTime.now()}",
                    extension = "jpg",
                    bytes = it.resize(
                        ResizeOptions(
                            maxWidth = 1080,
                            maxHeight = 1920,
                            thresholdBytes = Long.MAX_VALUE
                        )
                    )
                )
            }
        }
    )

    inner class CameraJsMessageHandler : IJsMessageHandler {

        override fun handle(
            message: JsMessage,
            navigator: WebViewNavigator?,
            callback: (String) -> Unit
        ) {
            mutableState.value = State.NavigateToCamera
        }

        override fun methodName(): String = BRIDGE_CAMERA_NAVIGATION_METHOD_NAME
    }

    fun initState() {
        mutableState.value = State.Init
    }

    companion object {
        private const val BRIDGE_CAMERA_NAVIGATION_METHOD_NAME = "onNavigateCamera"
        private const val BRIDGE_DOWNLOAD_IMAGE_METHOD_NAME = "onDownloadEndingImage"

        private const val CREATED_MEETING_URL = "upcoming-gathering"
        private const val ONGOING_MEETING_URL = "ongoing-gathering"
        private const val FINISHED_MEETING_URL = "ended-gathering"
        private const val COMPLETED_MEETING_URL = "memory-gathering"
        private const val ERROR_URL = "error"
        private const val MEETING_ID_KEY = "moimId"
    }
}
