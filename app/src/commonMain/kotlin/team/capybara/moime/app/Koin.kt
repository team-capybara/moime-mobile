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

package team.capybara.moime.app

import org.koin.core.context.startKoin
import team.capybara.moime.core.common.di.tokenModule
import team.capybara.moime.data.network.di.networkModule
import team.capybara.moime.data.network.di.repositoryModule
import team.capybara.moime.data.network.di.settingsModule
import team.capybara.moime.feature.di.screenModelModule

fun initKoin() = startKoin {
    modules(networkModule)
    modules(tokenModule)
    modules(settingsModule)
    modules(screenModelModule)
    modules(repositoryModule)
}
