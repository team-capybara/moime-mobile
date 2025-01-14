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

package team.capybara.moime.feature.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.hazeChild
import moime.core.designsystem.generated.resources.ic_notification
import moime.core.designsystem.generated.resources.ic_user_add
import team.capybara.moime.core.designsystem.component.MoimeIconButton
import team.capybara.moime.core.designsystem.theme.Gray700
import team.capybara.moime.core.ui.component.MoimeProfileImage
import team.capybara.moime.core.ui.component.TabViewSegmentedButtonBar
import team.capybara.moime.core.ui.compositionlocal.LocalHazeState
import team.capybara.moime.feature.home.HomeTabView
import team.capybara.moime.feature.main.MainTab
import team.capybara.moime.feature.main.MainTabView
import moime.core.designsystem.generated.resources.Res as MoimeRes

@Composable
fun MoimeMainTopAppBar(
    profileImageUrl: String,
    currentTab: MainTab,
    currentTabView: MainTabView,
    onClickProfile: () -> Unit,
    onClickUserAdd: () -> Unit,
    onClickNotification: () -> Unit,
    onTabViewChanged: (MainTabView) -> Unit,
    hasUnreadNotification: Boolean,
    hiddenBackground: Boolean,
) {
    val hazeState = LocalHazeState.current
    val animatedColor = animateColorAsState(
        if (hiddenBackground && currentTabView == HomeTabView.ListView) {
            Color.Transparent
        } else {
            BACKGROUND_COLOR
        }
    )
    val animatedBlur = animateDpAsState(
        if (hiddenBackground && currentTabView == HomeTabView.ListView) {
            (0.000001).dp
        } else {
            16.dp
        }
    )

    Surface(
        color = animatedColor.value,
        modifier = Modifier
            .then(
                if (!hiddenBackground || currentTabView == HomeTabView.CalendarView) {
                    Modifier.hazeChild(
                        state = hazeState,
                        style = HazeDefaults.style(
                            blurRadius = animatedBlur.value
                        )
                    )
                } else {
                    Modifier
                }
            )
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(HOME_TOP_APP_BAR_HEIGHT)
                .padding(top = 10.dp, bottom = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .safeDrawingPadding()
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MoimeProfileImage(
                    imageUrl = profileImageUrl,
                    size = 36.dp,
                    modifier = Modifier.clip(CircleShape).clickable { onClickProfile() }
                )
                Spacer(Modifier.weight(1f))
                MoimeIconButton(
                    onClick = onClickUserAdd,
                    iconRes = MoimeRes.drawable.ic_user_add,
                    tint = MaterialTheme.colorScheme.onBackground,
                    size = 24.dp
                )
                Spacer(Modifier.width(12.dp))
                MoimeIconButton(
                    onClick = onClickNotification,
                    iconRes = MoimeRes.drawable.ic_notification,
                    tint = MaterialTheme.colorScheme.onBackground,
                    size = 24.dp,
                    hasBadge = hasUnreadNotification
                )
            }
            TabViewSegmentedButtonBar(
                modifier = Modifier.align(Alignment.BottomCenter),
                tabViews = currentTab.tabViews,
                selected = currentTabView,
                onTabViewChanged = { onTabViewChanged(it) }
            )
        }
    }
}

val HOME_TOP_APP_BAR_HEIGHT = 136.dp
private val BACKGROUND_COLOR = Gray700.copy(alpha = 0.9f)