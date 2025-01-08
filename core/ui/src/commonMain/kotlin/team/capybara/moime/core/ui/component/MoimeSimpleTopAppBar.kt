package team.capybara.moime.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import team.capybara.moime.core.designsystem.component.MoimeIconButton
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray700

@Composable
fun MoimeSimpleTopAppBar(
    backIconRes: DrawableResource,
    onBack: () -> Unit,
    backgroundColor: Color = Gray700,
    contentColor: Color = Gray50,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.then(
            Modifier
                .height(HEIGHT)
                .fillMaxWidth()
                .background(backgroundColor)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MoimeIconButton(backIconRes, tint = contentColor, onClick = onBack)
    }
}

private val HEIGHT = 56.dp
