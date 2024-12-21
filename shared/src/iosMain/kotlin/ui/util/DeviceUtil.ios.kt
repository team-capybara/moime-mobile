package ui.util

import androidx.compose.runtime.Composable
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIUserInterfaceIdiomPhone
import platform.UIKit.UI_USER_INTERFACE_IDIOM

@Composable
actual fun getDeviceType(): DeviceType =
    when (UI_USER_INTERFACE_IDIOM()) {
        UIUserInterfaceIdiomPhone -> DeviceType.Phone
        UIUserInterfaceIdiomPad -> DeviceType.Tablet
        else -> DeviceType.Unknown
    }
