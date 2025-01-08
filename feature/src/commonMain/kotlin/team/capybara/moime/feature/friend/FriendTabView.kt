package team.capybara.moime.feature.friend

import androidx.compose.runtime.Composable
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.my_friend
import moime.feature.generated.resources.recommended_friend
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.ui.model.TabView

sealed class FriendTabView(
    override val titleTextRes: StringResource
) : TabView {
    data class MyFriend(
        val friendCount: Int,
        override val titleTextRes: StringResource = Res.string.my_friend
    ) : FriendTabView(titleTextRes) {

        @Composable
        override fun getTitleText() = stringResource(titleTextRes, friendCount)
    }

    data class RecommendedFriend(
        override val titleTextRes: StringResource = Res.string.recommended_friend
    ) : FriendTabView(titleTextRes)
}
