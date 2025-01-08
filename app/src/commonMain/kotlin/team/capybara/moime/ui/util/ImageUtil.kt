package team.capybara.moime.ui.util

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Compress with JPEG format and sampling the image with given options.
 */
expect fun ByteArray.resize(
    resizeOptions: ResizeOptions
): ByteArray

expect fun ByteArray.toImageBitmap(): ImageBitmap

data class ResizeOptions(
    val maxWidth: Int = 512,
    val maxHeight: Int = 512,
    val thresholdBytes: Long = 524288L
)
