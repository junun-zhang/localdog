package com.calsync.app.data.repository

import com.calsync.app.data.local.database.TaskDao
import com.calsync.app.data.local.entity.ReminderEntity
import com.calsync.app.data.local.entity.TaskEntity
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.model.CreateTaskRequest
import com.calsync.app.data.remote.model.UpdateTaskRequest
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val api: CalSyncApi
) {
    fun getTasksByCalendar(calendarId: String): Flow<List<Task>> =
        taskDao.getTasksByCalendar(calendarId).map { it.map { e -> e.toDomain() } }

    fun getTasksByStatus(status: Int): Flow<List<Task>> =
        taskDao.getTasksByStatus(status).map { it.map { e -> e.toDomain() } }

    suspend fun getTaskById(id: String): Task? = taskDao.getTaskById(id)?.toDomain()

    fun searchTasks(query: String): Flow<List<Task>> =
        taskDao.searchTasks(query).map { it.map { e -> e.toDomain() } }

    suspend fun createTask(task: Task): Result<Task> {
        taskDao.insertTask(task.toEntity())
        return try {
            val response = api.createTask(task.toCreateRequest())
            val body = response.body()
            if (response.isSuccessful && body?.success == true && body.data != null) {
                val dto = body.data
                val updated = task.copy(id = dto.id)
                taskDao.insertTask(updated.toEntity())
                Result.success(updated)
            } else Result.failure(Exception("Create failed: " + response.code()))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun updateTask(task: Task): Result<Task> {
        taskDao.insertTask(task.toEntity())
        return try {
            val response = api.updateTask(task.id, task.toUpdateRequest())
            val body = response.body()
            if (response.isSuccessful && body?.success == true) Result.success(task)
            else Result.failure(Exception("Update failed: " + response.code()))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deleteTask(task: Task): Result<Unit> {
        taskDao.deleteTask(task.toEntity())
        return try {
            val response = api.deleteTask(task.id)
            val body = response.body()
            if (response.isSuccessful && body?.success == true) Result.success(Unit)
            else Result.failure(Exception("Delete failed: " + response.code()))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun completeTask(id: String) { taskDao.completeTask(id) }

    private fun Task.toEntity() = TaskEntity(
        id, calendarId, title, description, dueDate, priority.ordinal, status.ordinal,
        reminders.map { r -> ReminderEntity(r.minutesBefore, r.enabled) }, eventId,
        isShared, createdBy, modifiedAt
    )

    private fun TaskEntity.toDomain() = Task(
        id, calendarId, title, description, dueDate,
        Task.Priority.entries.getOrElse(priority) { Task.Priority.MEDIUM },
        Task.TaskStatus.entries.getOrElse(status) { Task.TaskStatus.TODO },
        reminders.map { r -> Event.Reminder(r.minutesBefore, r.enabled) }, eventId,
        isShared, createdBy, modifiedAt
    )

    private fun Task.toCreateRequest() = CreateTaskRequest(
        calendarId, title, description, dueDate, priority.ordinal
    )

    private fun Task.toUpdateRequest() = UpdateTaskRequest(
        title, description, dueDate, priority.ordinal, status.ordinal, version
    )
}
