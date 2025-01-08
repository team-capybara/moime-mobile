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
