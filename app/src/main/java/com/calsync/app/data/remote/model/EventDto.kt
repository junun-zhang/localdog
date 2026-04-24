package com.calsync.app.data.remote.model
import com.google.gson.annotations.SerializedName
data class EventDto(
    @SerializedName("id") val id: String,
    @SerializedName("calendarId") val calendarId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("startTime") val startTime: Long,
    @SerializedName("endTime") val endTime: Long,
    @SerializedName("isAllDay") val isAllDay: Boolean,
    @SerializedName("color") val color: Int,
    @SerializedName("recurrenceRule") val recurrenceRule: String?,
    @SerializedName("reminders") val reminders: List<ReminderDto>?,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("version") val version: Int,
    @SerializedName("updatedAt") val updatedAt: Long
)
data class ReminderDto(@SerializedName("minutes") val minutes: Int)
data class CreateEventRequest(
    val calendarId: String, val title: String, val description: String?,
    val location: String?, val startTime: Long, val endTime: Long,
    val isAllDay: Boolean, val color: Int,
    val recurrenceRule: String?, val reminders: List<ReminderDto>?
)
data class UpdateEventRequest(
    val title: String, val description: String?, val location: String?,
    val startTime: Long, val endTime: Long, val isAllDay: Boolean,
    val color: Int, val recurrenceRule: String?, val reminders: List<ReminderDto>?,
    val version: Int
)
