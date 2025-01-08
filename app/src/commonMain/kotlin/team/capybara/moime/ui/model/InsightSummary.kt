package team.capybara.moime.ui.model

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate

data class InsightSummary(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val metFriends: List<Friend>,
    val meetingsCount: Map<DayOfWeek, Int>,
    val averagePeriod: DateTimePeriod
)
