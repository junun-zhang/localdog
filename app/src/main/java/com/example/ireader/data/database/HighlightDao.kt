package com.example.ireader.data.database

import androidx.room.*
import com.example.ireader.data.model.Highlight
import kotlinx.coroutines.flow.Flow

/**
 * 高亮数据访问对象
 */
@Dao
interface HighlightDao {
    
    @Query("SELECT * FROM highlights WHERE bookId = :bookId ORDER BY createdAt DESC")
    fun getHighlightsByBook(bookId: String): Flow<List<Highlight>>
    
    @Query("SELECT * FROM highlights ORDER BY createdAt DESC")
    fun getAllHighlights(): Flow<List<Highlight>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHighlight(highlight: Highlight): Long
    
    @Update
    suspend fun updateHighlight(highlight: Highlight)
    
    @Delete
    suspend fun deleteHighlight(highlight: Highlight)
    
    @Query("DELETE FROM highlights WHERE id = :id")
    suspend fun deleteHighlightById(id: String)
    
    @Query("DELETE FROM highlights WHERE bookId = :bookId")
    suspend fun deleteHighlightsByBook(bookId: String)
}