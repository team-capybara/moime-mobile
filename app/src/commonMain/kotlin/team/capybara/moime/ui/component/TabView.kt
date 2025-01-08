package team.capybara.moime.ui.component

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

interface TabView {
    val titleTextRes: StringResource

    @Composable
    fun getTitleText(): String = stringResource(titleTextRes)
}
