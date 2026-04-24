package com.calsync.app.domain.model
import java.util.UUID
data class Task(
    val id: String = UUID.randomUUID().toString(),
    val calendarId: String,
    val title: String,
    val description: String? = null,
    val dueDate: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val status: TaskStatus = TaskStatus.TODO,
    val reminders: List<Event.Reminder> = emptyList(),
    val eventId: String? = null,
    val isShared: Boolean = false,
    val createdBy: String = "",
    val modifiedAt: Long = System.currentTimeMillis(),
    val version: Int = 1
) {
    enum class Priority { NONE, LOW, MEDIUM, HIGH }
    enum class TaskStatus { TODO, IN_PROGRESS, DONE }
}
