package ui.model

import cafe.adriel.voyager.core.lifecycle.JavaSerializable
import kotlinx.datetime.LocalDateTime
import ui.util.DateUtil.isToday

data class Meeting(
    val id: Long,
    val title: String,
    val startDateTimeString: String,
    val finishDateTimeString: String?,
    val status: Status,
    val location: Location,
    val participants: List<Participant>,
    val thumbnailUrl: String? = null
) : JavaSerializable {

    val startDateTime: LocalDateTime
        get() = LocalDateTime.parse(startDateTimeString)

    val finishDateTime: LocalDateTime?
        get() = finishDateTimeString?.let { LocalDateTime.parse(it) }

    val isActive = startDateTime.isToday() && status != Status.Completed

    enum class Status(val value: String) {
        Created("CREATED"),
        Ongoing("ONGOING"),
        Finished("FINISHED"),
        Completed("COMPLETED"),
        Failed("FAILED"),
        Unknown("UNKNOWN")
        ;

        companion object {
            fun from(status: String) = entries.find { it.value == status } ?: Unknown
        }
    }
}
