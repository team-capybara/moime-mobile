package team.capybara.moime.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import team.capybara.moime.data.Api
import team.capybara.moime.data.model.ApiException
import team.capybara.moime.ui.repository.CameraRepository

class CameraRepositoryImpl(
    private val httpClient: HttpClient
) : CameraRepository {

    override suspend fun uploadImage(meetingId: Long, image: ByteArray): Result<Unit> =
        runCatching {
            httpClient.submitFormWithBinaryData(
                url = Api.MOIMS_PHOTO(meetingId),
                formData = formData {
                    append("file", image, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpg")
                        append(
                            HttpHeaders.ContentDisposition,
                            "filename=\"$meetingId-${Clock.System.now()}.jpg\""
                        )
                    })
                }
            ).also { if (it.status.value != 200) throw ApiException(it.status) }
        }
}
