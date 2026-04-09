package com.example.ireader.data.database

import androidx.room.*
import com.example.ireader.data.model.Bookmark
import kotlinx.coroutines.flow.Flow

/**
 * 书签数据访问对象
 */
@Dao
interface BookmarkDao {
    
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY createdAt DESC")
    fun getBookmarksByBook(bookId: String): Flow<List<Bookmark>>
    
    @Query("SELECT * FROM bookmarks WHERE bookId = :bookId ORDER BY createdAt DESC")
    suspend fun getBookmarksForBook(bookId: String): List<Bookmark>
    
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>
    
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    suspend fun getAllBookmarksList(): List<Bookmark>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: Bookmark)
    
    @Update
    suspend fun updateBookmark(bookmark: Bookmark)
    
    @Delete
    suspend fun deleteBookmark(bookmark: Bookmark)
    
    @Query("DELETE FROM bookmarks WHERE id = :bookmarkId")
    suspend fun deleteBookmarkById(bookmarkId: String)
    
    @Query("UPDATE bookmarks SET note = :note WHERE id = :bookmarkId")
    suspend fun updateBookmarkNote(bookmarkId: String, note: String)
    
    @Query("DELETE FROM bookmarks WHERE bookId = :bookId")
    suspend fun deleteBookmarksByBook(bookId: String)
}