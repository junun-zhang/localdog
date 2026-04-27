package com.example.ireader.data.repository

import com.example.ireader.data.local.dao.AnnotationDao
import com.example.ireader.data.local.dao.BookDao
import com.example.ireader.data.local.dao.BookmarkDao
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.local.entity.Annotation
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao,
    private val bookmarkDao: BookmarkDao,
    private val annotationDao: AnnotationDao
) {

    fun searchBooks(query: String): Flow<List<Book>> {
        return bookDao.searchBooks(query)
    }

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

    // === Bookmark operations ===

    fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>> {
        return bookmarkDao.getBookmarksForBook(bookId)
    }

    suspend fun addBookmark(bookId: String, chapterIndex: Int, position: String? = null): Bookmark {
        val bookmark = Bookmark(
            id = UUID.randomUUID().toString(),
            bookId = bookId,
            chapterIndex = chapterIndex,
            position = position,
            createTime = System.currentTimeMillis()
        )
        bookmarkDao.insertBookmark(bookmark)
        return bookmark
    }

    suspend fun deleteBookmark(bookmark: Bookmark) {
        bookmarkDao.deleteBookmark(bookmark)
    }

    suspend fun deleteBookmarkById(id: String) {
        bookmarkDao.deleteBookmarkById(id)
    }

    // === Annotation operations ===

    fun getAnnotationsForBook(bookId: String): Flow<List<Annotation>> {
        return annotationDao.getAnnotationsForBook(bookId)
    }

    suspend fun addAnnotation(
        bookId: String,
        chapterIndex: Int,
        highlightedText: String? = null,
        note: String? = null,
        color: Int = -16776961 // yellow
    ): Annotation {
        val now = System.currentTimeMillis()
        val annotation = Annotation(
            id = UUID.randomUUID().toString(),
            bookId = bookId,
            chapterIndex = chapterIndex,
            startPosition = "",
            endPosition = "",
            highlightedText = highlightedText,
            color = color,
            note = note,
            createTime = now,
            updateTime = now
        )
        annotationDao.insertAnnotation(annotation)
        return annotation
    }

    suspend fun updateAnnotationNote(annotationId: String, note: String) {
        val annotation = annotationDao.getAnnotationById(annotationId)
        annotation?.let {
            annotationDao.updateAnnotation(it.copy(note = note, updateTime = System.currentTimeMillis()))
        }
    }

    suspend fun deleteAnnotation(annotation: Annotation) {
        annotationDao.deleteAnnotation(annotation)
    }
}
