package di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.ParametersDefinition
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

    @Composable
    inline fun <reified T : ScreenModel> Screen.scopeScreenModel(noinline parameters: ParametersDefinition? = null): T {
        val currentParameters by rememberUpdatedState(parameters)
        val tag = remember(scope) { scope?.scopeQualifier?.value }
        return rememberScreenModel(tag = tag) {
            scope?.get<T>(parameters = currentParameters)
                ?: error("Scope is not created: ${T::class.simpleName}")
        }
    }

    private const val DEFAULT_SCOPE_NAME = "KOIN_SCOPE"
}
