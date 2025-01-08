package team.capybara.moime.core.ui.compositionlocal

import androidx.compose.runtime.compositionLocalOf

data class ScreenSize(val width: Int = 0, val height: Int = 0)

val LocalScreenSize = compositionLocalOf { ScreenSize() }
