package team.capybara.moime.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import org.koin.core.parameter.ParametersDefinition
import team.capybara.moime.core.common.di.ScopeProvider

object VoyagerUtil {
    @Composable
    inline fun <reified T : ScreenModel> Screen.scopeScreenModel(
        noinline parameters: ParametersDefinition? = null
    ): T {
        val currentParameters by rememberUpdatedState(parameters)
        val tag = remember(ScopeProvider.scope) { ScopeProvider.scope?.scopeQualifier?.value }
        return rememberScreenModel(tag = tag) {
            ScopeProvider.scope?.get<T>(parameters = currentParameters)
                ?: error("Scope is not created: ${T::class.simpleName}")
        }
    }
}
