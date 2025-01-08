package team.capybara.moime.feature.insight

import androidx.compose.runtime.Composable
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.insight_tab_friend
import moime.feature.generated.resources.insight_tab_summary
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.feature.main.MainTabView

enum class InsightTabView(
    override val titleTextRes: StringResource
) : MainTabView {
    Summary(
        titleTextRes = Res.string.insight_tab_summary
    ),
    Friend(
        titleTextRes = Res.string.insight_tab_friend
    );

    @Composable
    override fun getTitleText(): String = stringResource(titleTextRes)
}
