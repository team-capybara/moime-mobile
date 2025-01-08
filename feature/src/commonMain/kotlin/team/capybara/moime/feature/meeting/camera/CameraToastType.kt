package team.capybara.moime.feature.meeting.camera

import moime.feature.generated.resources.Res
import moime.feature.generated.resources.failed_to_capture
import moime.feature.generated.resources.failed_to_get_location
import moime.feature.generated.resources.loading_location
import moime.feature.generated.resources.upload_done
import moime.feature.generated.resources.upload_failure
import moime.feature.generated.resources.uploading_photo
import org.jetbrains.compose.resources.StringResource

enum class CameraToastType(
    val message: StringResource,
    val type: Type
) {
    LoadingLocation(
        message = Res.string.loading_location,
        type = Type.Normal
    ),
    LocationFailure(
        message = Res.string.failed_to_get_location,
        type = Type.Error
    ),
    CaptureFailure(
        message = Res.string.failed_to_capture,
        type = Type.Error
    ),
    UploadSuccess(
        message = Res.string.upload_done,
        type = Type.Normal
    ),
    UploadFailure(
        message = Res.string.upload_failure,
        type = Type.Error
    ),
    Uploading(
        message = Res.string.uploading_photo,
        type = Type.Normal
    );

    enum class Type {
        Normal, Error
    }
}
