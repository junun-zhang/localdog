package com.calsync.app.domain.model
import java.util.UUID
data class Event(
    val id: String = UUID.randomUUID().toString(),
    val calendarId: String,
    val title: String,
    val description: String? = null,
    val location: String? = null,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean = false,
    val color: Int = 0,
    val recurrenceRule: RecurrenceRule? = null,
    val reminders: List<Reminder> = emptyList(),
    val isShared: Boolean = false,
    val createdBy: String = "",
    val modifiedAt: Long = System.currentTimeMillis(),
    val version: Int = 1
) {
    data class Reminder(val minutesBefore: Int, val enabled: Boolean = true)
    fun getEventColor(): androidx.compose.ui.graphics.Color {
        val colors = listOf(
            com.calsync.app.ui.theme.EventBlue,
            com.calsync.app.ui.theme.EventRed,
            com.calsync.app.ui.theme.EventGreen,
            com.calsync.app.ui.theme.EventOrange,
            com.calsync.app.ui.theme.EventPurple,
            com.calsync.app.ui.theme.EventYellow
        )
        return colors.getOrElse(color % colors.size) { colors[0] }
    }
}
