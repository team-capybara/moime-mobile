package team.capybara.moime.core.ui.compositionlocal

import androidx.compose.runtime.compositionLocalOf
import team.capybara.moime.core.designsystem.component.ToastState

data class ToastHandler(
    val isShowing: Boolean,
    private val onShow: (ToastState) -> Unit
) {
    fun show(message: String, duration: Long = 2000L) {
        onShow(ToastState(message, duration))
    }
}

val LocalToastHandler = compositionLocalOf { ToastHandler(false){} }
