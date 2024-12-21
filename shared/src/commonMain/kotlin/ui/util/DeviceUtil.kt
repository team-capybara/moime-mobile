package ui.util

import androidx.compose.runtime.Composable

@Composable
expect fun getDeviceType(): DeviceType

enum class DeviceType {
    Phone,
    Tablet,
    Unknown,
}
