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

package team.capybara.moime.core.common.di

import org.koin.core.component.KoinComponent
import org.koin.core.scope.Scope

object ScopeProvider : KoinComponent {
    private val koin = getKoin()

    var scope: Scope? = null

    val isScopeCreated: Boolean
        get() = scope != null

    fun createScope(scopeName: String = DEFAULT_SCOPE_NAME) {
        scope = koin.createScope<String>(scopeName)
    }

    fun closeScope() {
        scope?.close()
        scope = null
    }

    private const val DEFAULT_SCOPE_NAME = "KOIN_SCOPE"
}
