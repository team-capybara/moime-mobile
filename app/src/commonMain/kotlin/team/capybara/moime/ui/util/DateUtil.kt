package team.capybara.moime.ui.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

object DateUtil {

    private val timeZone = TimeZone.currentSystemDefault()

    fun DayOfWeek.toKr(): String {
        return when (this.isoDayNumber) {
            1 -> "월"
            2 -> "화"
            3 -> "수"
            4 -> "목"
            5 -> "금"
            6 -> "토"
            7 -> "일"
            else -> throw IllegalArgumentException()
        }
    }

    fun LocalDateTime.toCompleteTime(): LocalDateTime {
        return toInstant(timeZone).plus(10, DateTimeUnit.MINUTE).toLocalDateTime(timeZone)
    }

    fun LocalDateTime.isPast(): Boolean {
        val now = LocalDateTime.now()
        return compareTo(now) < 0
    }

    fun LocalDateTime.getPeriodString(): String {
        val period = this.toInstant(timeZone).periodUntil(Clock.System.now(), timeZone)
        return period.formatToString()
    }

    fun LocalDateTime.daysUntilNow(): Int {
        val today = LocalDateTime.now()
        return date.daysUntil(today.date)
    }

    fun LocalDateTime.getDdayString(): String {
        if (isToday()) return "D-day"
        val diffInDays = daysUntilNow()
        return "D" + if (diffInDays > 0) {
            "+$diffInDays"
        } else if (diffInDays < 0) {
            diffInDays.toString()
        } else {
            "-day"
        }
    }

    fun LocalDateTime.isToday(): Boolean = date == LocalDateTime.now().date

    fun LocalDateTime.getMonthDayString(): String = format("M월 d일")

    fun LocalDateTime.getTimeString(): String = format("HH:mm")

    fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(timeZone)

    private fun Int.toFormattedPeriod() =
        abs(this).toString().padStart(2, '0')

    fun Int.secondsToPeriod(): DateTimePeriod {
        val hours = this / 3600
        val remainingSecondsAfterHours = this % 3600

        val minutes = remainingSecondsAfterHours / 60
        val remainingSeconds = remainingSecondsAfterHours % 60

        return DateTimePeriod(hours = hours, minutes = minutes, seconds = remainingSeconds)
    }

    fun DateTimePeriod.toSeconds(): Int {
        val hoursInSeconds = hours * 3600
        val minutesInSeconds = minutes * 60
        val totalSeconds = hoursInSeconds + minutesInSeconds + seconds
        return totalSeconds
    }

    fun DateTimePeriod.formatToString(): String {
        return "${hours.toFormattedPeriod()}:${minutes.toFormattedPeriod()}:${seconds.toFormattedPeriod()}"
    }
}

expect fun LocalDateTime.format(pattern: String): String