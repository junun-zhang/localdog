package com.example.ireader.data.database

import androidx.room.*
import com.example.ireader.data.model.Book
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for Book entity operations
 */
@Dao
interface BookDao {
    
    /**
     * Get all books from the database
     * @return Flow of list of books
     */
    @Query("SELECT * FROM books ORDER BY lastReadTime DESC")
    fun getAllBooks(): Flow<List<Book>>
    
    /**
     * Get all books from the database (one-shot query)
     * @return List of books
     */
    @Query("SELECT * FROM books ORDER BY lastReadTime DESC")
    suspend fun getAllBooksOnce(): List<Book>
    
    /**
     * Get a book by its ID
     * @param id Book ID
     * @return Book or null if not found
     */
    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): Book?
    
    /**
     * Insert a new book or update existing one
     * @param book Book to insert/update
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)
    
    /**
     * Insert multiple books
     * @param books List of books to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)
    
    /**
     * Update a book
     * @param book Book to update
     */
    @Update
    suspend fun updateBook(book: Book)
    
    /**
     * Update book reading progress
     * @param id Book ID
     * @param progress Reading progress (0-100)
     * @param lastReadPage Last read page number
     * @param lastReadTime Last read timestamp
     */
    @Query("UPDATE books SET progress = :progress, lastReadPage = :lastReadPage, lastReadTime = :lastReadTime WHERE id = :id")
    suspend fun updateBookProgress(id: String, progress: Int, lastReadPage: Int, lastReadTime: Long)
    
    /**
     * Delete a book by ID
     * @param id Book ID to delete
     */
    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBook(id: String)
    
    /**
     * Delete all books
     */
    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
    
    /**
     * Check if a book exists by file path
     * @param filePath File path to check
     * @return true if exists, false otherwise
     */
    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE filePath = :filePath)")
    suspend fun bookExists(filePath: String): Boolean
    
    /**
     * Get books by format
     * @param format File format (epub, pdf, txt)
     * @return List of books with specified format
     */
    @Query("SELECT * FROM books WHERE format = :format ORDER BY lastReadTime DESC")
    fun getBooksByFormat(format: String): Flow<List<Book>>
}