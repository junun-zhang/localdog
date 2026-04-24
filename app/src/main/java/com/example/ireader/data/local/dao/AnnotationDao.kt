package com.example.ireader.data.local.dao

import androidx.room.*
import com.example.ireader.data.local.entity.Annotation
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {
    @Query("SELECT * FROM annotations WHERE bookId = :bookId ORDER BY createTime DESC")
    fun getAnnotationsForBook(bookId: String): Flow<List<Annotation>>

    @Query("SELECT * FROM annotations WHERE id = :id")
    suspend fun getAnnotationById(id: String): Annotation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnotation(annotation: Annotation)

    @Update
    suspend fun updateAnnotation(annotation: Annotation)

    @Delete
    suspend fun deleteAnnotation(annotation: Annotation)

    @Query("DELETE FROM annotations WHERE id = :id")
    suspend fun deleteAnnotationById(id: String)
}
