package ui.friend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import di.ScopeProvider.scopeScreenModel
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.add_friend
import moime.shared.generated.resources.block
import moime.shared.generated.resources.day_count
import moime.shared.generated.resources.from_get_friend
import moime.shared.generated.resources.ic_calendar
import moime.shared.generated.resources.ic_close
import moime.shared.generated.resources.ic_timer
import moime.shared.generated.resources.meeting_count
import moime.shared.generated.resources.meeting_count_month
import moime.shared.generated.resources.meeting_current_month
import moime.shared.generated.resources.profile
import moime.shared.generated.resources.unblock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ui.component.ExceptionDialog
import ui.component.MoimeDialog
import ui.component.MoimeMeetingCard
import ui.component.MoimeProfileImage
import ui.component.MoimeSimpleTopAppBar
import ui.component.PaginationColumn
import ui.component.SafeAreaColumn
import ui.meeting.create.CreateScreen
import ui.meeting.detail.MeetingScreen
import ui.theme.Gray400
import ui.theme.Gray500
import ui.theme.MoimeGreen
import ui.theme.MoimeRed
import ui.util.DateUtil.daysUntilNow

data class FriendDetailScreen(
    private val targetId: Long,
) : Screen,
    KoinComponent {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val friendScreenModel = scopeScreenModel<FriendScreenModel>()
        val screenModel =
            rememberScreenModel {
                FriendDetailScreenModel(targetId, friendScreenModel, get(), get())
            }
        val state by screenModel.state.collectAsState()

        SafeAreaColumn {
            PaginationColumn(
                enablePaging = state.meetings.canRequest(),
                onPaging = { screenModel.loadMeetings() },
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            ) {
                item {
                    MoimeSimpleTopAppBar(
                        backIconRes = Res.drawable.ic_close,
                        onBack = { navigator.pop() },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(Res.string.profile),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 24.sp,
                        )
                        Text(
                            text =
                                stringResource(
                                    if (state.stranger.blocked) Res.string.unblock else Res.string.block,
                                ),
                            fontWeight = FontWeight.Normal,
                            color = MoimeRed,
                            fontSize = 16.sp,
                            modifier =
                                Modifier.clickable {
                                    if (state.stranger.blocked) {
                                        screenModel.unblock()
                                    } else {
                                        screenModel.block()
                                    }
                                },
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    MoimeProfileImage(
                        imageUrl = state.stranger.profileImageUrl,
                        size = 80.dp,
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = state.stranger.nickname,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                    )
                    Spacer(Modifier.height(16.dp))
                    if (state.stranger.friendshipDateTime == null) {
                        Button(
                            onClick = {
                                screenModel.addFriend {
                                    navigator.popUntilRoot()
                                    navigator.push(CreateScreen())
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp),
                            colors =
                                ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = MoimeGreen,
                                ),
                        ) {
                            Text(
                                text = stringResource(Res.string.add_friend),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                            )
                        }
                    } else {
                        Spacer(Modifier.height(36.dp))
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val daysUntilGetFriend = state.stranger.friendshipDateTime?.daysUntilNow()
                        FriendDetailCard(
                            leadingIconRes = Res.drawable.ic_calendar,
                            titleRes = Res.string.from_get_friend,
                            content = daysUntilGetFriend?.toString() ?: "--",
                            trailingStringRes = daysUntilGetFriend?.let { Res.string.day_count },
                            modifier = Modifier.weight(1f),
                        )
                        FriendDetailCard(
                            leadingIconRes = Res.drawable.ic_timer,
                            titleRes = Res.string.meeting_count_month,
                            content = state.meetingsTotalCount.toString(),
                            trailingStringRes = Res.string.meeting_count,
                            modifier = Modifier.weight(1f),
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            text = stringResource(Res.string.meeting_current_month),
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
                items(state.meetings.data) {
                    MoimeMeetingCard(
                        meeting = it,
                        onClick = { navigator.push(MeetingScreen(it)) },
                        isAnotherActiveMeetingCardFocusing = false,
                        forceDefaultHeightStyle = true,
                    )
                    Spacer(Modifier.height(8.dp))
                }
                item {
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
        state.dialogRequest?.let {
            MoimeDialog(
                request = it,
                onDismiss = { screenModel.hideDialog() },
            )
        }
        state.exception?.let {
            ExceptionDialog(
                exception = it,
                onDismiss = { screenModel.clearException() },
            )
        }
    }
}

@Composable
private fun FriendDetailCard(
    leadingIconRes: DrawableResource,
    titleRes: StringResource,
    content: String,
    trailingStringRes: StringResource?,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = Gray500,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.then(Modifier.height(76.dp)),
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painterResource(leadingIconRes),
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = stringResource(titleRes),
                    color = Gray400,
                    fontSize = 12.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = content,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                trailingStringRes?.let {
                    Text(
                        text = stringResource(it),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
