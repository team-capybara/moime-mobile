package ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.ic_profile_placeholder
import org.jetbrains.compose.resources.painterResource
import ui.theme.Gray500

@Composable
fun MoimeProfileImage(
    imageUrl: String,
    size: Dp? = null,
    enableBorder: Boolean = false,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        placeholder = painterResource(Res.drawable.ic_profile_placeholder),
        error = painterResource(Res.drawable.ic_profile_placeholder),
        modifier = modifier
            .then(if (enableBorder) Modifier.border(1.dp, Gray500, CircleShape) else Modifier)
            .then(size?.let { Modifier.size(it) } ?: Modifier)
            .then(Modifier.clip(CircleShape))
    )
}
