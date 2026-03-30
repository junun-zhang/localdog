package com.example.ireader.data.database

import androidx.room.*
import com.example.ireader.data.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * 笔记数据访问对象
 */
@Dao
interface NoteDao {
    
    @Query("SELECT * FROM notes WHERE bookId = :bookId ORDER BY createdTime DESC")
    fun getNotesByBook(bookId: String): Flow<List<Note>>
    
    @Query("SELECT * FROM notes ORDER BY createdTime DESC")
    fun getAllNotes(): Flow<List<Note>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)
    
    @Update
    suspend fun updateNote(note: Note)
    
    @Delete
    suspend fun deleteNote(note: Note)
    
    @Query("DELETE FROM notes WHERE bookId = :bookId")
    suspend fun deleteNotesByBook(bookId: String)
}