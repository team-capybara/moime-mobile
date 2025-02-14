/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package team.capybara.moime.feature.friend

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
import kotlinx.coroutines.launch
import moime.core.designsystem.generated.resources.ic_add
import moime.core.designsystem.generated.resources.ic_close
import moime.core.designsystem.generated.resources.ic_more
import moime.feature.generated.resources.Res
import moime.feature.generated.resources.add_friend
import moime.feature.generated.resources.add_friend_desc
import moime.feature.generated.resources.manage_blocked_friends
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.core.designsystem.component.MoimeDialog
import team.capybara.moime.core.designsystem.component.MoimeIconButton
import team.capybara.moime.core.designsystem.theme.Gray200
import team.capybara.moime.core.designsystem.theme.Gray50
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.model.User
import team.capybara.moime.core.ui.component.PaginationColumn
import team.capybara.moime.core.ui.component.SafeAreaColumn
import team.capybara.moime.core.ui.util.VoyagerUtil.scopeScreenModel
import team.capybara.moime.feature.component.MoimeFriendBar
import team.capybara.moime.feature.meeting.create.CreateScreen
import moime.core.designsystem.generated.resources.Res as MoimeRes

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
                                        MoimeIconButton(MoimeRes.drawable.ic_add) {
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
                                    MoimeIconButton(MoimeRes.drawable.ic_add) {
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
        MoimeIconButton(MoimeRes.drawable.ic_close, onClick = onClose)
        Box {
            MoimeIconButton(MoimeRes.drawable.ic_more) { menuExpanded = true }
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
