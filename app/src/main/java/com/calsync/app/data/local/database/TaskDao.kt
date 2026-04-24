package com.calsync.app.data.local.database
import androidx.room.*
import com.calsync.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE calendarId = :calendarId ORDER BY priority DESC, status ASC, dueDate ASC")
    fun getTasksByCalendar(calendarId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY priority DESC, dueDate ASC")
    fun getTasksByStatus(status: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: String): TaskEntity?

    @Query("SELECT * FROM tasks WHERE calendarId = :calendarId AND dueDate BETWEEN :start AND :end")
    fun getTasksInRange(calendarId: String, start: Long, end: Long): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)
    @Update suspend fun updateTask(task: TaskEntity)
    @Delete suspend fun deleteTask(task: TaskEntity)
    @Query("UPDATE tasks SET status = 2 WHERE id = :id")
    suspend fun completeTask(id: String)

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<TaskEntity>>
}
