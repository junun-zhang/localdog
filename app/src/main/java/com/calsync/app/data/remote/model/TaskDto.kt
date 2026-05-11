package com.calsync.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class TaskDto(
    @SerializedName("id") val id: String,
    @SerializedName("calendarId") val calendarId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("dueDate") val dueDate: Long?,
    @SerializedName("priority") val priority: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("reminders") val reminders: String?,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("version") val version: Int,
    @SerializedName("updatedAt") val updatedAt: String
)

data class CreateTaskRequest(
    val calendarId: String,
    val title: String,
    val description: String?,
    val dueDate: Long?,
    val priority: Int
)

data class UpdateTaskRequest(
    val title: String?,
    val description: String?,
    val dueDate: Long?,
    val priority: Int?,
    val status: Int?,
    val version: Int?
)
