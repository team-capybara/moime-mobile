package team.capybara.moime.data.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import team.capybara.moime.data.util.DateUtil.toIsoDateFormat
import team.capybara.moime.data.util.DateUtil.toIsoDateTimeFormat
import team.capybara.moime.ui.model.Location
import team.capybara.moime.ui.model.Meeting
import team.capybara.moime.ui.model.Participant

@Serializable
data class MeetingResponse(
    val data: List<MeetingResponseData>,
    val last: Boolean,
    val cursorId: CursorResponse?,
)

@Serializable
data class MeetingResponseData(
    val id: Long,
    val title: String,
    val startedAt: String,
    val finishedAt: String?,
    val location: LocationResponse,
    val status: String,
    val owner: ParticipantResponse,
    val participants: List<ParticipantResponse>,
    val bestPhotoUrl: String?,
) {
    fun toUiModel() =
        Meeting(
            id = id,
            title = title,
            startDateTimeString = startedAt.toIsoDateTimeFormat(),
            finishDateTimeString = finishedAt?.toIsoDateTimeFormat(),
            location = location.toUiModel(),
            status = Meeting.Status.from(status),
            participants = (listOf(owner) + participants).map { it.toUiModel() },
            thumbnailUrl = bestPhotoUrl,
        )
}

@Serializable
data class LocationResponse(
    val name: String,
    val latitude: Float,
    val longitude: Float,
) {
    fun toUiModel() =
        Location(
            name = name,
            lat = latitude,
            lng = longitude,
        )
}

@Serializable
data class ParticipantResponse(
    val userId: Long,
    val profileImageUrl: String,
) {
    fun toUiModel() =
        Participant(
            id = userId,
            profileImageUrl = profileImageUrl,
        )
}

@Serializable
data class CursorResponse(
    val cursorMoimId: Int,
    val cursorDate: String? = null,
)

@Serializable
data class MeetingCountResponse(
    val data: List<MeetingCountDataResponse>,
    val total: Int,
) {
    fun parse(): Map<LocalDate, Int> =
        data.associate {
            LocalDate.parse(it.date.toIsoDateFormat()) to it.count
        }
}

@Serializable
data class MeetingCountDataResponse(
    val date: String,
    val count: Int,
)

@Serializable
data class MeetingDateResponse(
    val data: List<MeetingResponseData>,
    val total: Int,
)

@Serializable
data class MeetingCountPerMonthResponse(
    val count: Int,
)
