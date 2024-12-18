package ui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.refTo
import kotlinx.cinterop.useContents
import org.jetbrains.skia.Image
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSize
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

actual fun ByteArray.resize(resizeOptions: ResizeOptions): ByteArray {
    return UIImage
        .imageWithData(toNSData())
        ?.fitInto(
            maxWidth = resizeOptions.maxWidth,
            maxHeight = resizeOptions.maxHeight,
            resizeThresholdBytes = resizeOptions.thresholdBytes
        )
        ?.toByteArray()
        ?: error ("Failed to resize image")
}

@OptIn(ExperimentalForeignApi::class)
private fun ByteArray.toNSData(): NSData = memScoped {
    return NSData.create(bytes = allocArrayOf(this@toNSData), length = this@toNSData.size.toULong())
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toByteArray(): ByteArray {
    val jpgData = UIImageJPEGRepresentation(this, 1.0)!!
    return ByteArray(jpgData.length.toInt()).apply {
        memcpy(this.refTo(0), jpgData.bytes, jpgData.length)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.fitInto(
    maxWidth: Int,
    maxHeight: Int,
    resizeThresholdBytes: Long
): UIImage {
    val imageData = this.toByteArray()
    return if (imageData.size > resizeThresholdBytes) {
        val originalWidth = this.size.useContents { width }
        val originalHeight = this.size.useContents { height }

        var inSampleSize = 1f
        while (originalWidth / inSampleSize > maxWidth ||
            originalHeight / inSampleSize > maxHeight) {
            inSampleSize *= 2
        }
        inSampleSize *= 2

        val newWidth = originalWidth / inSampleSize
        val newHeight = originalHeight / inSampleSize

        val targetSize = CGSizeMake(newWidth, newHeight)
        this.resize(targetSize)
    } else {
        this
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.resize(targetSize: CValue<CGSize>): UIImage {
    UIGraphicsBeginImageContextWithOptions(targetSize, false, 0.0)
    this.drawInRect(
        CGRectMake(
            0.0,
            0.0,
            targetSize.useContents { width },
            targetSize.useContents { height },
        ),
    )
    val newImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()

    return newImage!!
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return Image.makeFromEncoded(this).toComposeImageBitmap()
}
