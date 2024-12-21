package ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import ui.LocalScreenSize

@Composable
actual fun getDeviceType(): DeviceType =
    when (getDeviceWidthInDp()) {
        in 0f..599f -> DeviceType.Phone
        else -> DeviceType.Tablet
    }

@Composable
fun getDeviceWidthInDp(): Float {
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    return with(density) { screenSize.width.toDp().value }
}
