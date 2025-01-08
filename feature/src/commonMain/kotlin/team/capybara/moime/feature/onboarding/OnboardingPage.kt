package team.capybara.moime.feature.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.theme.Gray50

@Composable
fun OnboardingPage(
    step: OnboardingStep,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.then(Modifier.fillMaxSize().padding(horizontal = 20.dp))
    ) {
        Box(
            modifier = Modifier.weight(60f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(step.textRes),
                color = Gray50,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 30.sp
            )
        }
        Spacer(Modifier.weight(48f))
        Image(
            painter = painterResource(step.imageRes),
            contentDescription = null,
            modifier = Modifier.weight(350f).fillMaxWidth()
        )
    }
}
