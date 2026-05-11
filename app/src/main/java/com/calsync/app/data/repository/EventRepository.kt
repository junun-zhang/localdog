package com.calsync.app.data.repository

import com.calsync.app.data.local.database.EventDao
import com.calsync.app.data.local.entity.EventEntity
import com.calsync.app.data.local.entity.EventEntity.SyncStatus
import com.calsync.app.data.local.entity.ReminderEntity
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.model.CreateEventRequest
import com.calsync.app.data.remote.model.UpdateEventRequest
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.util.RecurrenceRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao,
    private val api: CalSyncApi
) {
    fun getEventsInRange(calendarId: String, start: Long, end: Long): Flow<List<Event>> =
        eventDao.getEventsInRange(calendarId, start, end).map { it.map { e -> e.toDomain() } }

    fun getAllEvents(calendarId: String): Flow<List<Event>> =
        eventDao.getAllEvents(calendarId).map { it.map { e -> e.toDomain() } }

    suspend fun getEventById(id: String): Event? = eventDao.getEventById(id)?.toDomain()

    fun searchEvents(query: String): Flow<List<Event>> =
        eventDao.searchEvents(query).map { it.map { e -> e.toDomain() } }

    suspend fun createEvent(event: Event): Result<Event> {
        val entity = event.toEntity()
        eventDao.insertEvent(entity.copy(syncStatus = SyncStatus.PENDING))
        return try {
            val response = api.createEvent(event.calendarId, event.toCreateRequest())
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                val dto = body.data
                val updated = event.copy(id = dto.id, version = dto.version)
                eventDao.insertEvent(updated.toEntity().copy(syncStatus = SyncStatus.SYNCED))
                Result.success(updated)
            } else {
                Result.success(event)
            }
        } catch (e: Exception) {
            Result.success(event)
        }
    }

    suspend fun updateEvent(event: Event): Result<Event> {
        eventDao.insertEvent(event.toEntity().copy(syncStatus = SyncStatus.PENDING))
        return try {
            val response = api.updateEvent(event.id, event.toUpdateRequest())
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                val dto = body.data
                val updated = event.copy(version = dto.version)
                eventDao.insertEvent(updated.toEntity().copy(syncStatus = SyncStatus.SYNCED))
                Result.success(updated)
            } else Result.success(event)
        } catch (e: Exception) { Result.success(event) }
    }

    suspend fun deleteEvent(event: Event): Result<Unit> {
        eventDao.deleteEventById(event.id)
        return try {
            val response = api.deleteEvent(event.id)
            val body = response.body()
            if (response.isSuccessful && body?.success == true) Result.success(Unit)
            else Result.failure(Exception("Delete failed"))
        } catch (e: Exception) { Result.success(Unit) }
    }

    private fun Event.toEntity() = EventEntity(
        id, calendarId, title, description, location, startTime, endTime, isAllDay, color,
        recurrenceRule?.toRRule(),
        reminders.map { ReminderEntity(it.minutesBefore, it.enabled) },
        isShared, createdBy, modifiedAt, SyncStatus.SYNCED
    )
    private fun EventEntity.toDomain() = Event(
        id, calendarId, title, description, location, startTime, endTime, isAllDay, color,
        recurrenceRule?.let { RecurrenceRule.fromRRule(it) },
        reminders.map { Event.Reminder(it.minutesBefore, it.enabled) },
        isShared, createdBy, modifiedAt
    )
    private fun Event.toCreateRequest() = CreateEventRequest(
        title, description, location, startTime, endTime, isAllDay, color,
        recurrenceRule?.toRRule(),
        remindersToJson(reminders)
    )
    private fun Event.toUpdateRequest() = UpdateEventRequest(
        title, description, location, startTime, endTime, isAllDay, color,
        recurrenceRule?.toRRule(),
        remindersToJson(reminders),
        version
    )
    private fun remindersToJson(reminders: List<Event.Reminder>): String? {
        if (reminders.isEmpty()) return null
        val arr = JSONArray()
        reminders.forEach { r ->
            arr.put(org.json.JSONObject().apply {
                put("minutesBefore", r.minutesBefore)
            })
        }
        return arr.toString()
    }
}
