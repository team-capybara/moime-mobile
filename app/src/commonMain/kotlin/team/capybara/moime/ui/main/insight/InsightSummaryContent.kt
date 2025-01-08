package team.capybara.moime.ui.main.insight

import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import team.capybara.moime.ui.LocalHazeState
import team.capybara.moime.ui.component.BOTTOM_NAV_BAR_HEIGHT
import team.capybara.moime.ui.component.HOME_TOP_APP_BAR_HEIGHT
import team.capybara.moime.ui.model.InsightSummary

@Composable
fun InsightSummaryContent(
    summary: InsightSummary,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val hazeState = LocalHazeState.current
    val listState = rememberLazyListState()
    val expandState = remember {
        mutableStateMapOf(*InsightSummaryType.entries.map { it to false }.toTypedArray())
    }

    LazyColumn(
        modifier = modifier.then(Modifier.fillMaxSize().haze(state = hazeState)),
        state = listState,
        contentPadding = PaddingValues(
            top = HOME_TOP_APP_BAR_HEIGHT + 8.dp,
            bottom = BOTTOM_NAV_BAR_HEIGHT + 8.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        item {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        }
        items(InsightSummaryType.entries.size) { index ->
            InsightSummaryCard(
                type = InsightSummaryType.entries[index],
                summary = summary,
                expanded = expandState[InsightSummaryType.entries[index]] ?: false,
                onExpand = { expanded ->
                    if (expanded) {
                        expandState.clear()
                        expandState[InsightSummaryType.entries[index]] = true
                        val scrollOffset = with(density) {
                            ((INSIGHT_SUMMARY_CARD_HEIGHT + 8.dp) * index).toPx()
                        }
                        scope.launch {
                            delay(10L)
                            with(listState) {
                                scrollToItem(0)
                                animateScrollBy(scrollOffset)
                            }
                        }
                    } else {
                        expandState[InsightSummaryType.entries[index]] = false
                    }
                }
            )
        }
        item {
            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}
