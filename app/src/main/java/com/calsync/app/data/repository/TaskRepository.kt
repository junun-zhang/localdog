package com.calsync.app.data.repository
import com.calsync.app.data.local.database.TaskDao
import com.calsync.app.data.local.entity.ReminderEntity
import com.calsync.app.data.local.entity.TaskEntity
import com.calsync.app.data.remote.api.CalSyncApi
import com.calsync.app.data.remote.model.CreateTaskRequest
import com.calsync.app.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
            if (response.isSuccessful) {
                val dto = response.body()!!
                val updated = task.copy(id = dto.id)
                taskDao.insertTask(updated.toEntity())
                Result.success(updated)
            } else Result.failure(Exception("Create failed: \${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun updateTask(task: Task): Result<Task> {
        taskDao.insertTask(task.toEntity())
        return try {
            val response = api.updateTask(task.id, task.toUpdateRequest())
            if (response.isSuccessful) Result.success(task)
            else Result.failure(Exception("Update failed: \${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun deleteTask(task: Task): Result<Unit> {
        taskDao.deleteTask(task.toEntity())
        return try {
            val response = api.deleteTask(task.id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Delete failed: \${response.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun completeTask(id: String) { taskDao.completeTask(id) }

    private fun Task.toEntity() = TaskEntity(
        id, calendarId, title, description, dueDate, priority.ordinal, status.ordinal,
        reminders.map { ReminderEntity(it.minutesBefore, it.enabled) }, eventId,
        isShared, createdBy, modifiedAt
    )
    private fun TaskEntity.toDomain() = Task(
        id, calendarId, title, description, dueDate,
        Task.Priority.entries.getOrElse(priority) { Task.Priority.MEDIUM },
        Task.TaskStatus.entries.getOrElse(status) { Task.TaskStatus.TODO },
        reminders.map { Task.Reminder(it.minutesBefore, it.enabled) }, eventId,
        isShared, createdBy, modifiedAt
    )
    private fun Task.toCreateRequest() = CreateTaskRequest(
        calendarId, title, description, dueDate, priority.ordinal, eventId
    )
    private fun Task.toUpdateRequest() = com.calsync.app.data.remote.model.UpdateTaskRequest(
        title, description, dueDate, priority.ordinal, status.ordinal, version
    )
}
