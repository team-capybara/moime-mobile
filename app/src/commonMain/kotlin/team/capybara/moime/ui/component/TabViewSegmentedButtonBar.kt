package team.capybara.moime.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import team.capybara.moime.ui.theme.Gray400
import team.capybara.moime.ui.theme.Gray50
import team.capybara.moime.ui.theme.Gray600
import team.capybara.moime.ui.theme.Gray800

@Composable
fun <T : TabView> TabViewSegmentedButtonBar(
    tabViews: List<T>,
    selected: T,
    onTabViewChanged: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.then(Modifier.height(SEGMENTED_BUTTON_BAR_HEIGHT)),
        shape = RoundedCornerShape(100.dp),
        color = Gray800
    ) {
        Row(
            modifier = Modifier.fillMaxHeight().padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            tabViews.forEach {
                TabViewSegmentedButton(
                    enabled = selected.titleTextRes != it.titleTextRes,
                    text = it.getTitleText(),
                    onClick = { onTabViewChanged(it) }
                )
            }
        }
    }
}

@Composable
private fun TabViewSegmentedButton(
    enabled: Boolean,
    text: String,
    onClick: (() -> Unit)?
) {
    Button(
        onClick = onClick ?: {},
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = Gray600,
            disabledContentColor = Gray50,
            containerColor = Gray800,
            contentColor = Gray400
        ),
        shape = RoundedCornerShape(100.dp),
        enabled = enabled
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )
    }
}

private val SEGMENTED_BUTTON_BAR_HEIGHT = 44.dp