package ui.util

import kotlinx.cinterop.BetaInteropApi
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import ui.util.Base64Util.encodeToBase64

actual object ShareUtil {

    actual fun shareText(text: String) {
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController
        val activityViewController = UIActivityViewController(listOf(text), null)
        currentViewController?.presentViewController(
            viewControllerToPresent = activityViewController,
            animated = true,
            completion = null
        )
    }

    @OptIn(BetaInteropApi::class)
    actual fun shareImage(image: ByteArray) {
        val nsData = NSData.create(image.encodeToBase64())
        val uiImage = UIImage(data = nsData ?: return)

        val activityViewController = UIActivityViewController(
            activityItems = listOf(uiImage),
            applicationActivities = null
        )

        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        rootViewController?.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}
