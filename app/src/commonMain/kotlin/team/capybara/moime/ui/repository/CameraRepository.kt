package team.capybara.moime.ui.repository

interface CameraRepository {

    suspend fun uploadImage(meetingId: Long, image: ByteArray): Result<Unit>
}
