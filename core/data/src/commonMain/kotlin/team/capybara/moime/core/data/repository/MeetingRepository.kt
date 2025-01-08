package team.capybara.moime.core.data.repository

import kotlinx.datetime.LocalDate
import team.capybara.moime.core.common.model.CursorData
import team.capybara.moime.core.common.model.CursorRequest
import team.capybara.moime.core.model.Meeting

interface MeetingRepository {
    suspend fun getAllMeetings(): Result<List<Meeting>>

    suspend fun getAllUpcomingMeetings(): Result<List<Meeting>>

    suspend fun getAllOngoingMeetings(): Result<List<Meeting>>

    suspend fun getAllCompletedMeetings(): Result<List<Meeting>>

    suspend fun getCompletedMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getOngoingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getUpcomingMeetings(cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getMeetingsCount(from: LocalDate, to: LocalDate): Result<Map<LocalDate, Int>>

    suspend fun getMeetingsOfDay(date: LocalDate): Result<List<Meeting>>

    suspend fun getMeetingsWith(targetId: Long, cursor: CursorRequest): Result<CursorData<Meeting>>

    suspend fun getMeetingsCountWith(targetId: Long): Result<Int>
}
