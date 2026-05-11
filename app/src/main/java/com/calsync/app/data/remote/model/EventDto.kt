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
    @SerializedName("reminders") val reminders: String?,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("version") val version: Int,
    @SerializedName("updatedAt") val updatedAt: String
)

data class CreateEventRequest(
    val title: String,
    val description: String?,
    val location: String?,
    val startTime: Long,
    val endTime: Long,
    val isAllDay: Boolean,
    val color: Int,
    val recurrenceRule: String?,
    val reminders: String?
)

data class UpdateEventRequest(
    val title: String?,
    val description: String?,
    val location: String?,
    val startTime: Long?,
    val endTime: Long?,
    val isAllDay: Boolean?,
    val color: Int?,
    val recurrenceRule: String?,
    val reminders: String?,
    val version: Int?
)
