package ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.api_exception
import moime.shared.generated.resources.confirm
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExceptionDialog(
    exception: Throwable,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    MoimeDialog(
        request = DialogRequest(
            title = stringResource(Res.string.api_exception),
            description = exception.message ?: exception.stackTraceToString(),
            actionTextRes = Res.string.confirm,
            onAction = onDismiss
        ),
        onDismiss = onDismiss,
        modifier = modifier
    )
}
