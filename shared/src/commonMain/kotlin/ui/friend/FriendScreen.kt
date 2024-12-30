package ui.friend

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import di.ScopeProvider.scopeScreenModel
import kotlinx.coroutines.launch
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.add_friend
import moime.shared.generated.resources.add_friend_desc
import moime.shared.generated.resources.ic_add
import moime.shared.generated.resources.ic_close
import moime.shared.generated.resources.ic_more
import moime.shared.generated.resources.manage_blocked_friends
import org.jetbrains.compose.resources.stringResource
import ui.component.ExceptionDialog
import ui.component.MoimeDialog
import ui.component.MoimeFriendBar
import ui.component.MoimeIconButton
import ui.component.PaginationColumn
import ui.component.SafeAreaColumn
import ui.meeting.create.CreateScreen
import ui.model.User
import ui.theme.Gray200
import ui.theme.Gray50
import ui.theme.Gray700

data class FriendScreen(
    val user: User?,
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()
        val friendScreenModel = scopeScreenModel<FriendScreenModel>()
        val friendState by friendScreenModel.state.collectAsState()

        var selectedTabView by remember {
            mutableStateOf<FriendTabView>(FriendTabView.MyFriend(friendState.friendsCount))
        }

        SafeAreaColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            FriendTopAppBar(
                onClose = { navigator.pop() },
                onClickBlockList = { navigator.push(FriendBlockListScreen(friendScreenModel)) },
            )
            PaginationColumn(
                enablePaging =
                    when (selectedTabView) {
                        is FriendTabView.MyFriend -> {
                            friendState.searchedMyFriends?.canRequest()
                                ?: friendState.myFriends.canRequest()
                        }

                        is FriendTabView.RecommendedFriend -> {
                            friendState.searchedRecommendedFriends?.canRequest()
                                ?: friendState.recommendedFriends.canRequest()
                        }
                    },
                onPaging = {
                    when (selectedTabView) {
                        is FriendTabView.MyFriend -> friendScreenModel.loadMyFriends()
                        is FriendTabView.RecommendedFriend -> friendScreenModel.loadRecommendedFriends()
                    }
                },
                contentPadding = PaddingValues(bottom = 4.dp),
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {
                item {
                    FriendTitle()
                    Spacer(Modifier.height(36.dp))
                    FriendInvitation(
                        userCode = user?.code ?: "",
                        profileImageUrl = user?.profileImageUrl ?: "",
                    )
                    Spacer(Modifier.height(30.dp))
                    FriendFindContent(
                        myCode = user?.code ?: "",
                        foundUser = friendState.foundUser,
                        onSearch = { friendScreenModel.findUser(it) },
                        onAddFriend = { targetFriend ->
                            friendScreenModel.addFriend(targetFriend) {
                                navigator.popUntilRoot()
                                navigator.push(CreateScreen())
                            }
                        },
                        onDismiss = { friendScreenModel.clearFoundUser() },
                    )
                    Spacer(Modifier.height(28.dp))
                    FriendListContentHeader(
                        tabViews =
                            listOf(
                                FriendTabView.MyFriend(friendState.friendsCount),
                                FriendTabView.RecommendedFriend(),
                            ),
                        selectedTabView = selectedTabView,
                        onTabViewChanged = { selectedTabView = it },
                        onSearch = {
                            coroutineScope.launch {
                                when (selectedTabView) {
                                    is FriendTabView.MyFriend ->
                                        friendScreenModel.searchMyFriends(it)

                                    is FriendTabView.RecommendedFriend ->
                                        friendScreenModel.searchRecommendedFriends(it)
                                }
                            }
                        },
                        onDismiss = {
                            when (selectedTabView) {
                                is FriendTabView.MyFriend -> friendScreenModel.clearSearchedMyFriends()
                                is FriendTabView.RecommendedFriend -> friendScreenModel.clearSearchedRecommendedFriends()
                            }
                        },
                    )
                    Spacer(Modifier.height(20.dp))
                }
                when (selectedTabView) {
                    is FriendTabView.MyFriend -> {
                        friendState.searchedMyFriends?.data?.let {
                            items(it) { searchedMyFriend ->
                                MoimeFriendBar(
                                    friend = searchedMyFriend,
                                    modifier =
                                        Modifier
                                            .clickable {
                                                navigator.push(
                                                    FriendDetailScreen(
                                                        searchedMyFriend.id,
                                                    ),
                                                )
                                            }.padding(start = 7.5.dp),
                                )
                                Spacer(Modifier.height(16.dp))
                            }
                        } ?: items(friendState.myFriends.data) { myFriend ->
                            MoimeFriendBar(
                                friend = myFriend,
                                modifier =
                                    Modifier
                                        .clickable { navigator.push(FriendDetailScreen(myFriend.id)) }
                                        .padding(start = 7.5.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                    is FriendTabView.RecommendedFriend -> {
                        friendState.searchedRecommendedFriends?.data?.let {
                            items(it) { searchedRecommendedFriend ->
                                MoimeFriendBar(
                                    friend = searchedRecommendedFriend,
                                    action = {
                                        MoimeIconButton(Res.drawable.ic_add) {
                                            friendScreenModel.addFriend(searchedRecommendedFriend) {
                                                navigator.popUntilRoot()
                                                navigator.push(CreateScreen())
                                            }
                                        }
                                    },
                                    modifier =
                                        Modifier
                                            .clickable {
                                                navigator.push(
                                                    FriendDetailScreen(
                                                        searchedRecommendedFriend.id,
                                                    ),
                                                )
                                            }.padding(start = 7.5.dp),
                                )
                                Spacer(Modifier.height(16.dp))
                            }
                        } ?: items(friendState.recommendedFriends.data) { recommendedFriend ->
                            MoimeFriendBar(
                                friend = recommendedFriend,
                                action = {
                                    MoimeIconButton(Res.drawable.ic_add) {
                                        friendScreenModel.addFriend(recommendedFriend) {
                                            navigator.popUntilRoot()
                                            navigator.push(CreateScreen())
                                        }
                                    }
                                },
                                modifier =
                                    Modifier
                                        .clickable { navigator.push(FriendDetailScreen(recommendedFriend.id)) }
                                        .padding(start = 7.5.dp),
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
        friendState.dialogRequest?.let {
            MoimeDialog(
                request = it,
                onDismiss = { friendScreenModel.hideDialog() },
            )
        }
        friendState.exception?.let {
            ExceptionDialog(
                exception = it,
                onDismiss = { friendScreenModel.clearException() },
            )
        }
    }
}

@Composable
private fun FriendTopAppBar(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onClickBlockList: () -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier =
            modifier.then(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MoimeIconButton(Res.drawable.ic_close, onClick = onClose)
        Box {
            MoimeIconButton(Res.drawable.ic_more) { menuExpanded = true }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                shape = RoundedCornerShape(8.dp),
                containerColor = Gray200,
            ) {
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = stringResource(Res.string.manage_blocked_friends),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Gray700,
                            )
                        }
                    },
                    onClick = {
                        menuExpanded = false
                        onClickBlockList()
                    },
                    modifier = Modifier.height(24.dp).width(102.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun FriendTitle(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(Res.string.add_friend),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Gray50,
        )
        Text(
            text = stringResource(Res.string.add_friend_desc),
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = Gray50,
        )
    }
}
