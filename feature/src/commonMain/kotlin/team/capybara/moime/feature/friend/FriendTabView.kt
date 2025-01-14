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
