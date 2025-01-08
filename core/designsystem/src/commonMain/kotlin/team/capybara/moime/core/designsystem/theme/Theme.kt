package team.capybara.moime.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val MoimeColorScheme = lightColorScheme(
    primary = Gray900,
    onPrimary = Gray50,
    secondary = Gray800,
    onSecondary = Gray50,
    tertiary = Gray700,
    onTertiary = Gray50,
    background = Gray700,
    onBackground = Gray50,
    surface = Gray800,
    onSurface = Gray50,
    surfaceContainer = Gray500,
    error = MoimeRed
)

@Composable
fun MoimeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MoimeColorScheme,
        typography = MoimeTypography(),
        content = content
    )
}
