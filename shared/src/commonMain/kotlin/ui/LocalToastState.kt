package ui

import androidx.compose.runtime.compositionLocalOf

data class ToastHandler(
    val isShowing: Boolean,
    private val onShow: (ToastState) -> Unit
) {
    fun show(message: String, duration: Long = 2000L) {
        onShow(ToastState(message, duration))
    }
}

val LocalToastHandler = compositionLocalOf { ToastHandler(false){} }

data class ToastState(
    val message: String = "",
    val duration: Long = 2000L
)
