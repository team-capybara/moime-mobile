package team.capybara.moime.ui.meeting.camera

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.internal.BackHandler
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import moime.app.generated.resources.Res
import moime.app.generated.resources.camera_permission_denied
import moime.app.generated.resources.camera_permission_help
import moime.app.generated.resources.ic_camera_filled
import moime.app.generated.resources.ic_close
import moime.app.generated.resources.ic_delete
import moime.app.generated.resources.ic_done
import moime.app.generated.resources.ic_moment
import moime.app.generated.resources.ic_refresh
import moime.app.generated.resources.ic_reload
import moime.app.generated.resources.ic_upload
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import team.capybara.moime.ui.component.SafeAreaColumn
import team.capybara.moime.ui.theme.Gray300
import team.capybara.moime.ui.theme.Gray400
import team.capybara.moime.ui.theme.Gray50
import team.capybara.moime.ui.theme.Gray500
import team.capybara.moime.ui.theme.Gray700
import team.capybara.moime.ui.theme.Gray800
import team.capybara.moime.ui.theme.MoimeGreen
import team.capybara.moime.ui.util.CameraMode
import team.capybara.moime.ui.util.PeekabooCamera
import team.capybara.moime.ui.util.rememberPeekabooCameraState

data class CameraScreen(val meetingId: Long) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val permissionFactory = rememberPermissionsControllerFactory()
        val permissionController = remember(permissionFactory) {
            permissionFactory.createPermissionsController()
        }
        val locationTrackerFactory = rememberLocationTrackerFactory(LocationTrackerAccuracy.Best)
        val locationTracker = remember(locationTrackerFactory) {
            locationTrackerFactory.createLocationTracker(permissionController)
        }
        val cameraScreenModel = rememberScreenModel {
            CameraScreenModel(meetingId, locationTracker)
        }
        val uiState by cameraScreenModel.state.collectAsState()
        val cameraState = rememberPeekabooCameraState(
            onCapture = cameraScreenModel::onCaptured
        )

        BindEffect(permissionController)

        BindLocationTrackerEffect(locationTracker)

        LaunchedEffect(Unit) {
            val permissions = listOf(Permission.CAMERA, Permission.LOCATION)
            permissions.forEach {
                if (permissionController.isPermissionGranted(it).not()) {
                    permissionController.providePermission(it)
                }
            }
            cameraScreenModel.startLocationTracker()
        }

        BackHandler(cameraState.isCapturing.not()) {
            navigator.pop()
            cameraScreenModel.stopLocationTracker()
            cameraScreenModel.clear()
        }

        SafeAreaColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        navigator.pop()
                        cameraScreenModel.stopLocationTracker()
                        cameraScreenModel.clear()
                    }
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_close),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Spacer(Modifier.weight(76f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                PeekabooCamera(
                    state = cameraState,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    permissionDeniedContent = { CameraPermissionDeniedContent() }
                )
                androidx.compose.animation.AnimatedVisibility(
                    visible = cameraState.isCapturing,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Gray800.copy(alpha = 0.75f))
                            .clip(RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = MoimeGreen,
                            strokeWidth = 2.dp
                        )
                    }
                }
                uiState.photo?.let { photo ->
                    Image(
                        photo,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp))
                            .graphicsLayer {
                                this.rotationY =
                                    if (cameraState.cameraMode == CameraMode.Front) 180f else 0f
                            }
                    )
                }
                if (uiState.photo != null &&
                    (uiState.uploadState == CameraUploadState.Init ||
                            uiState.uploadState == CameraUploadState.Failure)) {
                    FilledIconButton(
                        onClick = { cameraScreenModel.clear() },
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .size(36.dp)
                            .align(Alignment.BottomCenter),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Gray500,
                            contentColor = Gray50
                        )
                    ) {
                        Icon(
                            painterResource(Res.drawable.ic_delete),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(48f))
                Row(
                    modifier = Modifier.fillMaxWidth().weight(80f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    if (uiState.photo == null) {
                        IconButton(
                            onClick = {},
                            enabled = with(cameraState) { isCameraReady && isCapturing.not() }
                        ) {
                            Icon(
                                painterResource(Res.drawable.ic_moment),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .scale(56 / 80f),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                    if (uiState.photo != null) {
                        FilledIconButton(
                            onClick = {
                                if (uiState.uploadState != CameraUploadState.Uploading) {
                                    cameraScreenModel.uploadPhoto()
                                }
                            },
                            modifier = Modifier.fillMaxHeight().aspectRatio(1f),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MoimeGreen,
                                contentColor = Gray700,
                                disabledContainerColor = Gray800,
                                disabledContentColor = Gray50
                            ),
                            enabled = uiState.uploadState != CameraUploadState.Success
                        ) {
                            if (uiState.uploadState == CameraUploadState.Uploading) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .aspectRatio(1f)
                                        .scale(28 / 80f),
                                    color = Gray700,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                when (uiState.uploadState) {
                                    CameraUploadState.Init -> Res.drawable.ic_upload
                                    CameraUploadState.Success -> Res.drawable.ic_done
                                    CameraUploadState.Failure -> Res.drawable.ic_reload
                                    CameraUploadState.Uploading -> null
                                }?.let {
                                    Icon(
                                        painterResource(it),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .aspectRatio(1f)
                                            .scale(36 / 80f)
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(width = 4.dp, color = MoimeGreen, shape = CircleShape)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            FilledTonalButton(
                                onClick = { cameraState.capture() },
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .scale(64 / 80f),
                                shape = CircleShape,
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = Gray50,
                                    disabledContainerColor = Gray300
                                ),
                                enabled = with(cameraState) {
                                    isCameraReady && isCapturing.not() && uiState.location != null
                                }
                            ) {}
                        }
                    }
                    if (uiState.photo == null) {
                        IconButton(
                            onClick = { cameraState.toggleCamera() },
                            enabled = with(cameraState) { isCameraReady && isCapturing.not() }
                        ) {
                            Icon(
                                painterResource(Res.drawable.ic_refresh),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .scale(56 / 80f),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(40f))
                uiState.toast?.let { CameraToast(it) } ?: Spacer(Modifier.weight(36f))
                Spacer(Modifier.weight(80f))
            }
        }
}

@Composable
private fun CameraPermissionDeniedContent() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(20.dp),
        color = Gray800
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(Res.drawable.ic_camera_filled),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Gray50
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Res.string.camera_permission_denied),
                fontWeight = FontWeight.Bold,
                color = Gray50,
                fontSize = 20.sp
            )
            Text(
                text = stringResource(Res.string.camera_permission_help),
                fontWeight = FontWeight.Normal,
                color = Gray400,
                fontSize = 12.sp
            )
        }
    }
}
