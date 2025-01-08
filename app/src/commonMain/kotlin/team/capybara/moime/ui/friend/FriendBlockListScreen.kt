package team.capybara.moime.ui.friend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import moime.app.generated.resources.Res
import moime.app.generated.resources.ic_chevron_left
import moime.app.generated.resources.manage_blocked_friends
import moime.app.generated.resources.people_count
import moime.app.generated.resources.unblock
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.ui.component.MoimeFriendBar
import team.capybara.moime.ui.component.MoimeSimpleTopAppBar
import team.capybara.moime.ui.component.PaginationColumn
import team.capybara.moime.ui.component.SafeAreaColumn
import team.capybara.moime.ui.theme.Gray400
import team.capybara.moime.ui.theme.Gray50
import team.capybara.moime.ui.theme.Gray600
import team.capybara.moime.ui.theme.MoimeRed

data class FriendBlockListScreen(
    private val friendScreenModel: FriendScreenModel
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val friendState by friendScreenModel.state.collectAsState()

        SafeAreaColumn(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            MoimeSimpleTopAppBar(
                backIconRes = Res.drawable.ic_chevron_left,
                onBack = { navigator.pop() }
            )
            FriendBlockListHeader(count = friendState.blockedFriendsCount)
            Spacer(Modifier.height(12.dp))
            PaginationColumn(
                enablePaging = friendState.blockedFriends.canRequest(),
                onPaging = { friendScreenModel.loadBlockedFriends() },
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 24.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
            ) {
                items(friendState.blockedFriends.data) { friend ->
                    MoimeFriendBar(
                        friend = friend,
                        action = {
                            Button(
                                onClick = { friendScreenModel.unblockFriend(friend.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gray600,
                                    contentColor = MoimeRed
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
                            ) {
                                Text(
                                    stringResource(Res.string.unblock),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                )
                            }
                        },
                        modifier = Modifier.clickable {
                            navigator.push(FriendDetailScreen(friend.id))
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun FriendBlockListHeader(
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.manage_blocked_friends),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Gray50
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = stringResource(Res.string.people_count, count),
            color = Gray400,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}
