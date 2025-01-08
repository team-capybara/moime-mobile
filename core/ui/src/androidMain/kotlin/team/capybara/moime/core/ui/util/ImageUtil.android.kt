package team.capybara.moime.core.ui.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ByteArray.resize(
    resizeOptions: ResizeOptions
): ByteArray {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }

    BitmapFactory.decodeByteArray(this, 0, size, options)

    var inSampleSize = 1
    while (options.outWidth / inSampleSize > resizeOptions.maxWidth ||
        options.outHeight / inSampleSize > resizeOptions.maxHeight
    ) {
        inSampleSize *= 2
    }

    options.inJustDecodeBounds = false
    options.inSampleSize = inSampleSize

    val resizedBitmap = BitmapFactory.decodeByteArray(this, 0, size, options)

    ByteArrayOutputStream().use { byteArrayOutputStream ->
        resizedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            byteArrayOutputStream
        )
        return byteArrayOutputStream.toByteArray()
    }
}

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, size).asImageBitmap()
}
