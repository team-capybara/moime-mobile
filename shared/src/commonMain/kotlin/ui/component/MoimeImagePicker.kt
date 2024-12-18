package ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ui.util.ResizeOptions
import ui.util.resize

@Composable
fun MoimeImagePicker(
    onPicked: (ByteArray) -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
    resizeOptions: ResizeOptions = ResizeOptions(512, 512, 512L)
) {
    val imagePicker = rememberFilePickerLauncher(
        type = PickerType.Image,
        onResult = { result ->
            scope.launch {
                val byteArray = result?.readBytes()?.resize(resizeOptions) ?: return@launch
                onPicked(byteArray)
            }
        }
    )
    scope.launch { imagePicker.launch() }
}
