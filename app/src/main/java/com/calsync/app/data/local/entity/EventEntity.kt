package com.calsync.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "events")
@TypeConverters(ReminderTypeConverter::class)
data class EventEntity(
    @PrimaryKey val id: String,
    val calendarId: String,
    val title: String,
    val description: String?,
    val location: String?,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean,
    val color: Int,
    val recurrenceRule: String?,
    val reminders: List<ReminderEntity>,
    val isShared: Boolean,
    val createdBy: String,
    val modifiedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
) {
    enum class SyncStatus { SYNCED, PENDING, DELETED }
}

data class ReminderEntity(
    val minutesBefore: Int,
    val enabled: Boolean = true
)

class ReminderTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(value: List<ReminderEntity>): String = gson.toJson(value)

    @TypeConverter
    fun toList(value: String): List<ReminderEntity> =
        gson.fromJson(value, object : TypeToken<List<ReminderEntity>>() {}.type) ?: emptyList()
}
