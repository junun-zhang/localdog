package com.calsync.app.data.local.entity
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "events")
@TypeConverters(ReminderTypeConverter::class)
data class EventEntity(
    @PrimaryKey val id: String, val calendarId: String, val title: String,
    val description: String?, val location: String?, val startTime: Long,
    val endTime: Long, val isAllDay: Boolean, val color: Int,
    val recurrenceRule: String?, val reminders: List<ReminderEntity>,
    val isShared: Boolean, val createdBy: String, val modifiedAt: Long,
    val syncStatus: SyncStatus = SyncStatus.SYNCED
) {
    enum class SyncStatus { SYNCED, PENDING, DELETED }
}
data class ReminderEntity(val minutesBefore: Int, val enabled: Boolean = true)
class ReminderTypeConverter {
    private val gson = Gson()
    fun fromList(value: List<ReminderEntity>): String = gson.toJson(value)
    fun toList(value: String): List<ReminderEntity> = gson.fromJson(value, object : TypeToken<List<ReminderEntity>>() {}.type) ?: emptyList()
}
