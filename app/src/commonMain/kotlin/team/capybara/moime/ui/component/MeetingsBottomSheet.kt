package team.capybara.moime.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import team.capybara.moime.ui.model.Meeting
import team.capybara.moime.ui.theme.Gray800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsBottomSheet(
    meetings: List<Meeting>,
    onClickMeeting: (Meeting) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = Gray800
    ) {
        Column(
            modifier = modifier.then(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
            )
        ) {
            meetings.forEach {
                MoimeMeetingCard(
                    meeting = it,
                    onClick = {
                        onClickMeeting(it)
                        onDismissRequest()
                    },
                    isAnotherActiveMeetingCardFocusing = false,
                    forceDefaultHeightStyle = true,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}
