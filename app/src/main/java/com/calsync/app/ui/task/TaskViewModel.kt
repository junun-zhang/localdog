package com.calsync.app.ui.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.repository.TaskRepository
import com.calsync.app.domain.model.Task
import com.calsync.app.domain.model.Task.Priority
import com.calsync.app.domain.model.Task.TaskStatus
import com.calsync.app.domain.model.Event.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val activeFilter: TaskFilter = TaskFilter.ALL,
    val isLoading: Boolean = false
)

enum class TaskFilter { ALL, TODO, IN_PROGRESS, DONE }

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TaskUiState())
    val state: StateFlow<TaskUiState> = _state.asStateFlow()

    private val calendarId = "default"

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            taskRepository.getTasksByCalendar(calendarId)
                .collect { tasks ->
                    _state.update { it.copy(tasks = tasks, isLoading = false) }
                }
        }
    }

    fun getFilteredTasks(): List<Task> {
        val all = _state.value.tasks
        return when (_state.value.activeFilter) {
            TaskFilter.ALL -> all
            TaskFilter.TODO -> all.filter { it.status == TaskStatus.TODO }
            TaskFilter.IN_PROGRESS -> all.filter { it.status == TaskStatus.IN_PROGRESS }
            TaskFilter.DONE -> all.filter { it.status == TaskStatus.DONE }
        }
    }

    fun setFilter(filter: TaskFilter) {
        _state.update { it.copy(activeFilter = filter) }
    }

    fun toggleTaskStatus(task: Task) {
        val newStatus = when (task.status) {
            TaskStatus.TODO -> TaskStatus.IN_PROGRESS
            TaskStatus.IN_PROGRESS -> TaskStatus.DONE
            TaskStatus.DONE -> TaskStatus.TODO
        }
        val updated = task.copy(status = newStatus, modifiedAt = System.currentTimeMillis())
        viewModelScope.launch {
            taskRepository.updateTask(updated)
        }
    }

    fun completeTask(taskId: String) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId) ?: return@launch
            val updated = task.copy(status = TaskStatus.DONE, modifiedAt = System.currentTimeMillis())
            taskRepository.updateTask(updated)
        }
    }

    fun batchCompleteTasks(taskIds: List<String>) {
        viewModelScope.launch {
            for (id in taskIds) {
                val task = taskRepository.getTaskById(id) ?: continue
                val updated = task.copy(status = TaskStatus.DONE, modifiedAt = System.currentTimeMillis())
                taskRepository.updateTask(updated)
            }
        }
    }

    fun batchDeleteTasks(tasks: List<Task>) {
        viewModelScope.launch {
            for (task in tasks) {
                taskRepository.deleteTask(task)
            }
        }
    }

    fun createTask(
        title: String,
        description: String? = null,
        dueDate: Long? = null,
        priority: Priority = Priority.MEDIUM,
        onComplete: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID().toString(),
                calendarId = calendarId,
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority
            )
            val result = taskRepository.createTask(task)
            onComplete(result.isSuccess)
        }
    }

    fun updateTask(task: Task, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val updated = task.copy(modifiedAt = System.currentTimeMillis())
            val result = taskRepository.updateTask(updated)
            onComplete(result.isSuccess)
        }
    }

    fun deleteTask(task: Task, onComplete: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val result = taskRepository.deleteTask(task)
            onComplete(result.isSuccess)
        }
    }

    fun getTaskById(taskId: String, onResult: (Task?) -> Unit) {
        viewModelScope.launch {
            val task = taskRepository.getTaskById(taskId)
            onResult(task)
        }
    }
}
