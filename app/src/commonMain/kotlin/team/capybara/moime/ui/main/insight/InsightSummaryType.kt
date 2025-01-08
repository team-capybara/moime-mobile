package team.capybara.moime.ui.main.insight

import moime.app.generated.resources.Res
import moime.app.generated.resources.insight_title_friend
import moime.app.generated.resources.insight_title_meeting
import moime.app.generated.resources.insight_title_time
import org.jetbrains.compose.resources.StringResource

enum class InsightSummaryType(
    val titleRes: StringResource
) {
    Friend(Res.string.insight_title_friend),
    Meeting(Res.string.insight_title_meeting),
    Time(Res.string.insight_title_time)
}
