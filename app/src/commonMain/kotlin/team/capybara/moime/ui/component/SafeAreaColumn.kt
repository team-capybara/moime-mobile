package team.capybara.moime.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import team.capybara.moime.ui.theme.Gray700

@Composable
fun SafeAreaColumn(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    backgroundColor: Color = Gray700,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val density = LocalDensity.current
    val topPadding = with(density) { WindowInsets.statusBars.getTop(this).toDp() }
    val bottomPadding = with(density) { WindowInsets.navigationBars.getBottom(this).toDp() }

    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .padding(
                top = topPadding,
                bottom = bottomPadding
            )
            .fillMaxSize()
            .then(modifier),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}
