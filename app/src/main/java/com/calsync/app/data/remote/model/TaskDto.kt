package com.calsync.app.data.remote.model
data class TaskDto(
    val id: String, val calendarId: String, val title: String,
    val description: String?, val dueDate: Long?, val priority: Int,
    val status: Int, val eventId: String?, val createdBy: String,
    val version: Int, val updatedAt: Long
)
data class CreateTaskRequest(
    val calendarId: String, val title: String, val description: String?,
    val dueDate: Long?, val priority: Int, val eventId: String?
)
data class UpdateTaskRequest(
    val title: String, val description: String?, val dueDate: Long?,
    val priority: Int, val status: Int, val version: Int
)
