package com.calsync.app.data.local.database
import androidx.room.*
import com.calsync.app.data.local.entity.CalendarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDao {
    @Query("SELECT * FROM calendars")
    fun getAllCalendars(): Flow<List<CalendarEntity>>

    @Query("SELECT * FROM calendars WHERE id = :id")
    suspend fun getCalendarById(id: String): CalendarEntity?

    @Query("SELECT * FROM calendars WHERE isVisible = 1")
    fun getVisibleCalendars(): Flow<List<CalendarEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendar(calendar: CalendarEntity)
    @Update suspend fun updateCalendar(calendar: CalendarEntity)
    @Delete suspend fun deleteCalendar(calendar: CalendarEntity)
}
