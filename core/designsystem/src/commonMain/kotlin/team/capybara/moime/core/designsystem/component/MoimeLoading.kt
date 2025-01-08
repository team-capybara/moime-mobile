package team.capybara.moime.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import team.capybara.moime.core.designsystem.theme.MoimeGreen

@Composable
fun MoimeLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.then(Modifier.fillMaxSize()),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MoimeGreen)
    }
}
