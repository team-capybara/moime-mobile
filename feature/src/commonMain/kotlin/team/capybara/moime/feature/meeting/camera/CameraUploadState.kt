package team.capybara.moime.feature.meeting.camera

sealed interface CameraUploadState {
    data object Init : CameraUploadState
    data object Uploading : CameraUploadState
    data object Success : CameraUploadState
    data object Failure : CameraUploadState
}
