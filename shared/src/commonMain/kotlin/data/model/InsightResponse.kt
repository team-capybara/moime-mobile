package data.model

import data.util.DateUtil.toIsoDateFormat
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import ui.model.Friend
import ui.model.InsightSummary
import ui.model.Survey
import ui.util.DateUtil.secondsToPeriod

@Serializable
data class InsightSummaryResponse(
    val startDate: String,
    val endDate: String,
    val friends: List<InsightSummaryFriendResponse>,
    val moimCount: InsightSummaryMeetingsCountResponse,
    val averageMoimSeconds: Int
) {
    fun toUiModel() = InsightSummary(
        startDate = LocalDate.parse(startDate.toIsoDateFormat()),
        endDate = LocalDate.parse(endDate.toIsoDateFormat()),
        metFriends = friends.map { it.toUiModel() },
        meetingsCount = moimCount.toUiModel(),
        averagePeriod = averageMoimSeconds.secondsToPeriod()
    )
}

@Serializable
data class InsightSummaryFriendResponse(
    val id: Long,
    val profile: String
) {
    fun toUiModel() = Friend(id, "", "", profile, null, false)
}

@Serializable
data class InsightSummaryMeetingsCountResponse(
    val MONDAY: Int = 0,
    val TUESDAY: Int = 0,
    val WEDNESDAY: Int = 0,
    val THURSDAY: Int = 0,
    val FRIDAY: Int = 0,
    val SATURDAY: Int = 0,
    val SUNDAY: Int = 0
) {
    fun toUiModel() = mapOf(
        DayOfWeek.MONDAY to MONDAY,
        DayOfWeek.TUESDAY to TUESDAY,
        DayOfWeek.WEDNESDAY to WEDNESDAY,
        DayOfWeek.THURSDAY to THURSDAY,
        DayOfWeek.FRIDAY to FRIDAY,
        DayOfWeek.SATURDAY to SATURDAY,
        DayOfWeek.SUNDAY to SUNDAY
    )
}

@Serializable
data class SurveyResponse(
    val count: Int,
    val submitted: Boolean
) {
    fun toUiModel() = Survey(count, submitted)
}
