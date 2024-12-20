package ui.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIModalPresentationPopover
import platform.UIKit.UIPopoverArrowDirectionUp
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIUserInterfaceIdiomPhone
import platform.UIKit.UI_USER_INTERFACE_IDIOM
import platform.UIKit.popoverPresentationController
import ui.util.Base64Util.encodeToBase64

actual object ShareUtil {

    @OptIn(ExperimentalForeignApi::class)
    actual fun shareText(text: String) {
        val currentViewController = UIApplication.sharedApplication().keyWindow?.rootViewController ?: return
        val activityViewController = UIActivityViewController(listOf(text), null)
        when(UI_USER_INTERFACE_IDIOM()) {
            UIUserInterfaceIdiomPad -> {
                with(activityViewController) {
                    setPreferredContentSize(CGSizeMake(200.0, 200.0))
                    modalPresentationStyle = UIModalPresentationPopover
                }
                with(activityViewController.popoverPresentationController!!) {
                    permittedArrowDirections = UIPopoverArrowDirectionUp
                    sourceView = currentViewController.view
                    sourceRect = currentViewController.view.frame
                    currentViewController.presentViewController(
                        viewControllerToPresent = activityViewController,
                        animated = true,
                        completion = null
                    )
                }
            }
            UIUserInterfaceIdiomPhone -> {
                currentViewController.presentViewController(
                    viewControllerToPresent = activityViewController,
                    animated = true,
                    completion = null
                )
            }
            else -> { /* Unsupported device */}
        }
    }

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
