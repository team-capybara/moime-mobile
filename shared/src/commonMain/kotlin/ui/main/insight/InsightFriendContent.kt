package ui.main.insight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.survey_desc
import moime.shared.generated.resources.survey_title
import org.jetbrains.compose.resources.stringResource
import ui.LocalScreenSize
import ui.component.SurveyButton
import ui.model.Survey
import ui.theme.Gray400
import ui.theme.Gray50

@Composable
fun InsightFriendContent(
    survey: Survey,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val screenSize = LocalScreenSize.current
    val buttonSize = with(density) { (screenSize.width / 2).toDp() }

    Column(
        modifier = modifier.then(Modifier.fillMaxSize()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SurveyButton(
            onClick = { onSubmit() },
            survey = survey,
            modifier = Modifier.size(buttonSize)
        )
        Spacer(Modifier.height(28.dp))
        Text(
            text = stringResource(Res.string.survey_title),
            color = Gray50,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.survey_desc),
            color = Gray400,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
