package team.capybara.moime.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import team.capybara.moime.ui.theme.Gray200

@Composable
fun MoimeImagePlaceholder(
    size: Dp?,
    shape: Shape,
    color: Color = Gray200,
    border: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = shape,
        color = color,
        border = border,
        modifier = modifier.then(size?.let { Modifier.size(size) } ?: Modifier)
    ) {}
}
