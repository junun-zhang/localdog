package com.example.ireader.data.repository

import com.example.ireader.data.local.dao.BookDao
import com.example.ireader.data.local.entity.Book
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
) {

    fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks()
    }

    suspend fun getBookById(bookId: String): Book? {
        return bookDao.getBookById(bookId)
    }

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun insertBooks(books: List<Book>) {
        bookDao.insertBooks(books)
    }

    suspend fun updateBook(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: Book) {
        bookDao.deleteBook(book)
    }

    suspend fun deleteBookById(bookId: String) {
        bookDao.deleteBookById(bookId)
    }

    /**
     * 根据文件路径创建并插入新书
     */
    suspend fun addLocalBook(
        filePath: String,
        title: String,
        author: String? = null,
        format: String,
        fileSize: Long
    ): Book {
        val bookId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val book = Book(
            id = bookId,
            title = title,
            author = author,
            format = format,
            filePath = filePath,
            coverPath = null,
            fileSize = fileSize,
            addTime = now,
            lastReadTime = now,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = true,
            bookSource = "local"
        )
        insertBook(book)
        return book
    }

    suspend fun updateReadingProgress(bookId: String, chapter: Int, position: Int, progress: Float) {
        val book = getBookById(bookId)?.copy(
            currentChapter = chapter,
            progress = progress,
            lastReadTime = System.currentTimeMillis()
        )
        book?.let { updateBook(it) }
    }
}
