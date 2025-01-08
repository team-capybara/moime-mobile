package team.capybara.moime.ui.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter

actual fun LocalDateTime.format(pattern: String): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateFormat = pattern
    return dateFormatter.stringFromDate(toInstant(TimeZone.currentSystemDefault()).toNSDate())
}
