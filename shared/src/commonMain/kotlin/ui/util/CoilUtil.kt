package ui.util

import coil3.PlatformContext
import coil3.SingletonImageLoader

object CoilUtil {
    fun clearMemoryCache(context: PlatformContext) {
        SingletonImageLoader.get(context).memoryCache?.clear()
    }

    fun clearDiskCache(context: PlatformContext) {
        SingletonImageLoader.get(context).diskCache?.clear()
    }
}
