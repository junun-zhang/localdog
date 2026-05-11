package com.calsync.app.data.repository
import com.calsync.app.data.local.database.CalendarDao
import com.calsync.app.data.local.entity.CalendarEntity
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.model.CreateCalendarRequest
import com.calsync.app.data.remote.model.JoinRequest
import com.calsync.app.domain.model.Calendar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val calendarDao: CalendarDao,
    private val api: CalSyncApi
) {
    fun getAllCalendars(): Flow<List<Calendar>> =
        calendarDao.getAllCalendars().map { it.map { e -> e.toDomain() } }

    fun getVisibleCalendars(): Flow<List<Calendar>> =
        calendarDao.getVisibleCalendars().map { it.map { e -> e.toDomain() } }

    suspend fun getCalendarById(id: String): Calendar? = calendarDao.getCalendarById(id)?.toDomain()

    suspend fun createCalendar(name: String, color: Int): Result<Calendar> = try {
        val response = api.createCalendar(CreateCalendarRequest(name, color))
        val body = response.body()
        if (response.isSuccessful && body?.success == true && body.data != null) {
            val dto = body.data
            val cal = Calendar(dto.id, name, color, true, true, dto.ownerId,
                Calendar.CalendarRole.OWNER, dto.inviteCode)
            calendarDao.insertCalendar(cal.toEntity())
            Result.success(cal)
        } else Result.failure(Exception("Create failed: ${response.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun joinCalendar(inviteCode: String): Result<Calendar> = try {
        val response = api.joinCalendar(JoinRequest(inviteCode))
        val body = response.body()
        if (response.isSuccessful && body?.success == true && body.data != null) {
            val dto = body.data
            val cal = Calendar(dto.id, dto.name, dto.color, true, true, dto.ownerId,
                Calendar.CalendarRole.VIEWER, dto.inviteCode)
            calendarDao.insertCalendar(cal.toEntity())
            Result.success(cal)
        } else Result.failure(Exception("Join failed: ${response.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    suspend fun deleteCalendar(id: String): Result<Unit> = try {
        val response = api.deleteCalendar(id)
        val body = response.body()
        if (response.isSuccessful && body?.success == true) {
            calendarDao.getCalendarById(id)?.let { calendarDao.deleteCalendar(it) }
            Result.success(Unit)
        } else Result.failure(Exception("Delete failed: ${response.code()}"))
    } catch (e: Exception) { Result.failure(e) }

    private fun CalendarEntity.toDomain() = Calendar(
        id, name, color, isVisible, isShared, ownerUserId,
        Calendar.CalendarRole.entries.getOrElse(role) { Calendar.CalendarRole.VIEWER }, inviteCode
    )
    private fun Calendar.toEntity() = CalendarEntity(
        id, name, color, isVisible, isShared, ownerUserId, role.ordinal, inviteCode
    )
}
