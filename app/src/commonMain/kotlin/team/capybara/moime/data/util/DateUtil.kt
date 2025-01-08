package team.capybara.moime.data.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

object DateUtil {
    fun String.toIsoDateTimeFormat(): String {
        val year = substring(0, 4)
        val month = substring(4, 6)
        val day = substring(6, 8)
        val hour = substring(8, 10)
        val minute = substring(10, 12)
        val second = substring(12, 14)

        return "$year-$month-${day}T$hour:$minute:$second"
    }

    fun String.toIsoDateFormat(): String {
        val year = substring(0, 4)
        val month = substring(4, 6)
        val day = substring(6, 8)

        return "$year-$month-$day"
    }

    fun LocalDateTime.toApiFormat(): String {
        val year = year.toString().padStart(4, '0')
        val month = monthNumber.toString().padStart(2, '0')
        val day = dayOfMonth.toString().padStart(2, '0')
        val hour = hour.toString().padStart(2, '0')
        val minute = minute.toString().padStart(2, '0')
        val second = second.toString().padStart(2, '0')

        return "$year$month$day$hour$minute$second"
    }

    fun LocalDate.toApiFormat(): String {
        val year = year.toString().padStart(4, '0')
        val month = monthNumber.toString().padStart(2, '0')
        val day = dayOfMonth.toString().padStart(2, '0')

        return "$year$month$day"
    }
}
