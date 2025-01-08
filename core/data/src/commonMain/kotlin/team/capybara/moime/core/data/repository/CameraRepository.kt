package team.capybara.moime.core.data.repository

interface CameraRepository {

    suspend fun uploadImage(meetingId: Long, image: ByteArray): Result<Unit>
}
