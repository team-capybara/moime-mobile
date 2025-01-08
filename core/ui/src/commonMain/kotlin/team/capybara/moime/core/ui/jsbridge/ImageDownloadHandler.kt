package team.capybara.moime.core.ui.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import team.capybara.moime.core.common.util.Base64Util.decodeFromBase64

class ImageDownloadHandler(
    private val methodName: String = DEFAULT_BRIDGE_METHOD_NAME,
    private val onDownload: (ByteArray) -> Unit
) : IJsMessageHandler {

    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit
    ) {
        val imageJsMessage = processParams<ImageStringData>(message)
        onDownload(imageJsMessage.image.decodeFromBase64())
        callback(CALLBACK)
    }

    override fun methodName(): String = methodName

    companion object {
        private const val DEFAULT_BRIDGE_METHOD_NAME = "onDownloadImage"
        private const val CALLBACK = "SUCCESS"
    }
}
