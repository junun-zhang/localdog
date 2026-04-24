package com.calsync.app.data.local.entity
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "tasks")
@TypeConverters(TaskReminderTypeConverter::class)
data class TaskEntity(
    @PrimaryKey val id: String, val calendarId: String, val title: String,
    val description: String?, val dueDate: Long?, val priority: Int,
    val status: Int, val reminders: List<ReminderEntity>, val eventId: String?,
    val isShared: Boolean, val createdBy: String, val modifiedAt: Long,
    val syncStatus: Int = 0
)
class TaskReminderTypeConverter {
    private val gson = Gson()
    fun fromList(value: List<ReminderEntity>): String = gson.toJson(value)
    fun toList(value: String): List<ReminderEntity> = gson.fromJson(value, object : TypeToken<List<ReminderEntity>>() {}.type) ?: emptyList()
}
