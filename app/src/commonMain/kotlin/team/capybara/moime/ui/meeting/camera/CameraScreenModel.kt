package team.capybara.moime.ui.meeting.camera

import androidx.compose.ui.graphics.ImageBitmap
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.ashampoo.kim.Kim
import com.ashampoo.kim.model.GpsCoordinates
import com.ashampoo.kim.model.MetadataUpdate
import dev.icerock.moko.geo.LatLng
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import team.capybara.moime.ui.repository.CameraRepository
import team.capybara.moime.ui.util.ResizeOptions
import team.capybara.moime.ui.util.resize
import team.capybara.moime.ui.util.toImageBitmap

class CameraScreenModel(
    private val meetingId: Long,
    private val locationTracker: LocationTracker
) : StateScreenModel<CameraScreenModel.CameraState>(CameraState()), KoinComponent {

    private val cameraRepository: CameraRepository by inject()
    private var photoByteArray: ByteArray? = null

    data class CameraState(
        val location: LatLng? = null,
        val photo: ImageBitmap? = null,
        val uploadState: CameraUploadState = CameraUploadState.Init,
        val toast: CameraToastType? = CameraToastType.LoadingLocation
    )

    init {
        locationTracker.getLocationsFlow()
            .distinctUntilChanged()
            .onEach {
                if (state.value.location == null) {
                    mutableState.value = state.value.copy(toast = null)
                }
                mutableState.value = state.value.copy(location = it)
            }
            .launchIn(screenModelScope)
    }

    fun startLocationTracker() {
        screenModelScope.launch {
            locationTracker.startTracking()
        }
    }

    fun stopLocationTracker() {
        locationTracker.stopTracking()
    }

    fun onCaptured(capturedPhoto: ByteArray?) {
        screenModelScope.launch {
            capturedPhoto?.let { image ->
                mutableState.value = state.value.copy(photo = image.toImageBitmap())
                photoByteArray = image
            } ?: run {
                mutableState.value = state.value.copy(toast = CameraToastType.CaptureFailure)
            }
        }
    }

    private fun processImage(
        location: LatLng,
        imageByteArray: ByteArray,
        resizeOptions: ResizeOptions = ResizeOptions()
    ): ByteArray {
        val resizedImage = imageByteArray.resize(resizeOptions)
        val manipulatedImage = Kim.update(
            bytes = resizedImage,
            update = MetadataUpdate.GpsCoordinates(
                gpsCoordinates = GpsCoordinates(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            ),
        )
        return manipulatedImage
    }

    fun uploadPhoto() {
        photoByteArray?.let { photo ->
            state.value.location?.let { location ->
                screenModelScope.launch {
                    mutableState.value = state.value.copy(
                        uploadState = CameraUploadState.Uploading,
                        toast = CameraToastType.Uploading
                    )
                    cameraRepository.uploadImage(
                        meetingId = meetingId,
                        image = processImage(location, photo)
                    ).onSuccess {
                        mutableState.value = state.value.copy(
                            uploadState = CameraUploadState.Success,
                            toast = CameraToastType.UploadSuccess
                        )
                        delay(800L) // reset state after uploading photo successfully
                        clear()
                    }.onFailure {
                        mutableState.value = state.value.copy(
                            uploadState = CameraUploadState.Failure,
                            toast = CameraToastType.UploadFailure
                        )
                    }
                }
            } ?: run {
                mutableState.value = state.value.copy(toast = CameraToastType.LocationFailure)
            }
        } ?: run {
            mutableState.value = state.value.copy(toast = CameraToastType.UploadFailure)
        }
    }

    fun clear() {
        mutableState.value = state.value.copy(
            photo = null,
            uploadState = CameraUploadState.Init,
            toast = null
        )
        photoByteArray = null
    }
}
