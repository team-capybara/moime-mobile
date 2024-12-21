package ui.friend

import Platform
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import getPlatform
import kotlinx.coroutines.launch
import moime.shared.generated.resources.Res
import moime.shared.generated.resources.app_share_content_text
import moime.shared.generated.resources.cannot_share_on_ipad
import moime.shared.generated.resources.ic_export
import moime.shared.generated.resources.invite_app
import moime.shared.generated.resources.invite_app_desc
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.LocalToastHandler
import ui.component.MoimeProfileImage
import ui.theme.Gray500
import ui.util.DeviceType
import ui.util.ShareUtil
import ui.util.getDeviceType

@Composable
fun FriendInvitation(
    userCode: String,
    profileImageUrl: String,
    modifier: Modifier = Modifier,
) {
    val toast = LocalToastHandler.current
    val scope = rememberCoroutineScope()
    val isIpad = getPlatform() == Platform.iOS && getDeviceType() == DeviceType.Tablet

    Column(modifier = modifier.then(Modifier.fillMaxWidth())) {
        Text(
            stringResource(Res.string.invite_app),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(12.dp))
        Surface(
            color = Gray500,
            shape = RoundedCornerShape(12.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        scope.launch {
                            if (isIpad) {
                                toast.show(getString(Res.string.cannot_share_on_ipad))
                            } else {
                                ShareUtil.shareText(
                                    getString(Res.string.app_share_content_text, userCode),
                                )
                            }
                        }
                    },
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                MoimeProfileImage(
                    imageUrl = profileImageUrl,
                    size = 40.dp,
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    text = stringResource(Res.string.invite_app_desc),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painterResource(Res.drawable.ic_export),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}
