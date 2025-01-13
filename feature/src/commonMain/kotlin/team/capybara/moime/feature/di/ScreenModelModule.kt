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

package team.capybara.moime.feature.di

import org.koin.dsl.module
import team.capybara.moime.feature.friend.FriendScreenModel
import team.capybara.moime.feature.home.HomeScreenModel
import team.capybara.moime.feature.insight.InsightScreenModel
import team.capybara.moime.feature.login.LoginScreenModel
import team.capybara.moime.feature.main.MainScreenModel
import team.capybara.moime.feature.splash.SplashScreenModel

val screenModelModule = module {
    single { SplashScreenModel(get()) }
    single { LoginScreenModel(get()) }

    scope<String> {
        scoped { MainScreenModel(get(), get()) }
        scoped { HomeScreenModel(get()) }
        scoped { FriendScreenModel(get()) }
        scoped { InsightScreenModel(get()) }
    }
}
