package team.capybara.moime.feature.insight

import moime.feature.generated.resources.Res
import moime.feature.generated.resources.insight_title_friend
import moime.feature.generated.resources.insight_title_meeting
import moime.feature.generated.resources.insight_title_time
import org.jetbrains.compose.resources.StringResource

enum class InsightSummaryType(
    val titleRes: StringResource
) {
    Friend(Res.string.insight_title_friend),
    Meeting(Res.string.insight_title_meeting),
    Time(Res.string.insight_title_time)
}
