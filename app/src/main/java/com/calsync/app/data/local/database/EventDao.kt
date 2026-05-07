package com.calsync.app.data.local.database

import androidx.room.*
import com.calsync.app.data.local.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events WHERE calendarId = :calendarId AND startTime < :end AND endTime > :start ORDER BY startTime")
    fun getEventsInRange(calendarId: String, start: Long, end: Long): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: String): EventEntity?

    @Query("SELECT * FROM events WHERE calendarId = :calendarId ORDER BY startTime DESC")
    fun getAllEvents(calendarId: String): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE syncStatus != 0")
    suspend fun getUnsyncedEvents(): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: String)

    @Query("SELECT * FROM events WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchEvents(query: String): Flow<List<EventEntity>>
}
