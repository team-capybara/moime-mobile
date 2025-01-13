/*
 * Copyright 2025 Yeojun Yoon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package team.capybara.moime.data.network.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import team.capybara.moime.core.data.repository.CameraRepository
import team.capybara.moime.data.network.Api
import team.capybara.moime.data.network.model.ApiException

class DefaultCameraRepository(
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
