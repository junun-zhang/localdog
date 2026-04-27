package com.example.ireader.data.repository

import com.example.ireader.data.local.dao.AnnotationDao
import com.example.ireader.data.local.dao.BookDao
import com.example.ireader.data.local.dao.BookmarkDao
import com.example.ireader.data.local.dao.UserSettingsDao
import com.example.ireader.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BookRepositoryTest {

    private class FakeBookDao : BookDao {
        val books = mutableListOf<Book>()
        override fun getAllBooks(): Flow<List<Book>> = flowOf(books.toList())
        override suspend fun getBookById(bookId: String): Book? = books.find { it.id == bookId }
        override suspend fun insertBook(book: Book) { books.removeAll { it.id == book.id }; books.add(book) }
        override suspend fun insertBooks(books: List<Book>) { books.forEach { insertBook(it) } }
        override suspend fun updateBook(book: Book) { books.removeAll { it.id == book.id }; books.add(book) }
        override suspend fun deleteBook(book: Book) { books.remove(book) }
        override suspend fun deleteBookById(bookId: String) { books.removeAll { it.id == bookId } }
        override suspend fun getBookCount(): Int = books.size
        override fun searchBooks(query: String): Flow<List<Book>> =
            flowOf(books.filter { it.title?.contains(query, ignoreCase = true) == true }.toList())
    }

    private class FakeBookmarkDao : BookmarkDao {
        val bookmarks = mutableListOf<Bookmark>()
        override fun getBookmarksForBook(bookId: String): Flow<List<Bookmark>> =
            flowOf(bookmarks.filter { it.bookId == bookId }.toList())
        override suspend fun getBookmarkById(id: String): Bookmark? = bookmarks.find { it.id == id }
        override suspend fun insertBookmark(bookmark: Bookmark) { bookmarks.removeAll { it.id == bookmark.id }; bookmarks.add(bookmark) }
        override suspend fun updateBookmark(bookmark: Bookmark) { bookmarks.removeAll { it.id == bookmark.id }; bookmarks.add(bookmark) }
        override suspend fun deleteBookmark(bookmark: Bookmark) { bookmarks.remove(bookmark) }
        override suspend fun deleteBookmarkById(id: String) { bookmarks.removeAll { it.id == id } }
    }

    private class FakeAnnotationDao : AnnotationDao {
        val annotations = mutableListOf<com.example.ireader.data.local.entity.Annotation>()
        override fun getAnnotationsForBook(bookId: String): Flow<List<com.example.ireader.data.local.entity.Annotation>> =
            flowOf(annotations.filter { it.bookId == bookId }.toList())
        override suspend fun getAnnotationById(id: String): com.example.ireader.data.local.entity.Annotation? = annotations.find { it.id == id }
        override suspend fun insertAnnotation(annotation: com.example.ireader.data.local.entity.Annotation) { annotations.removeAll { it.id == annotation.id }; annotations.add(annotation) }
        override suspend fun updateAnnotation(annotation: com.example.ireader.data.local.entity.Annotation) { annotations.removeAll { it.id == annotation.id }; annotations.add(annotation) }
        override suspend fun deleteAnnotation(annotation: com.example.ireader.data.local.entity.Annotation) { annotations.remove(annotation) }
        override suspend fun deleteAnnotationById(id: String) { annotations.removeAll { it.id == id } }
    }

    private class FakeUserSettingsDao : UserSettingsDao {
        val settings = mutableMapOf<String, UserSettings>()
        override fun getSetting(key: String): Flow<UserSettings?> = flowOf(settings[key])
        override suspend fun getSettingSync(key: String): UserSettings? = settings[key]
        override suspend fun insertSetting(setting: UserSettings) { settings[setting.key] = setting }
        override suspend fun deleteSetting(setting: UserSettings) { settings.remove(setting.key) }
        override fun getAllSettings(): Flow<List<UserSettings>> = flowOf(settings.values.toList())
    }

    private lateinit var bookDao: FakeBookDao
    private lateinit var bookmarkDao: FakeBookmarkDao
    private lateinit var annotationDao: FakeAnnotationDao
    private lateinit var userSettingsDao: FakeUserSettingsDao
    private lateinit var repository: BookRepository

    private fun createTestBook(id: String = "test-id"): Book {
        return Book(
            id = id, title = "Test Book", author = null, format = "txt",
            filePath = "/tmp/test.txt", coverPath = null, fileSize = 1000,
            addTime = System.currentTimeMillis(), lastReadTime = System.currentTimeMillis(),
            currentChapter = 0, progress = 0f, isDownloaded = true, bookSource = "local"
        )
    }

    @Before
    fun setup() {
        bookDao = FakeBookDao()
        bookmarkDao = FakeBookmarkDao()
        annotationDao = FakeAnnotationDao()
        userSettingsDao = FakeUserSettingsDao()
        repository = BookRepository(bookDao, bookmarkDao, annotationDao, userSettingsDao)
    }

    @Test
    fun `getAllBooks returns books from dao`() = runTest {
        bookDao.books.add(createTestBook("1"))
        bookDao.books.add(createTestBook("2"))
        val result = repository.getAllBooks().first()
        assertEquals(2, result.size)
    }

    @Test
    fun `getBookById returns book when exists`() = runTest {
        bookDao.books.add(createTestBook("book-1"))
        val result = repository.getBookById("book-1")
        assertEquals("book-1", result?.id)
    }

    @Test
    fun `getBookById returns null when not found`() = runTest {
        val result = repository.getBookById("nonexistent")
        assertNull(result)
    }

    @Test
    fun `addLocalBook creates book with correct fields`() = runTest {
        val book = repository.addLocalBook("/tmp/book.txt", "New Book", format = "txt", fileSize = 5000)
        assertNotNull(book.id)
        assertEquals("New Book", book.title)
        assertEquals("txt", book.format)
        assertEquals(5000L, book.fileSize)
    }

    @Test
    fun `updateReadingProgress updates chapter and progress`() = runTest {
        bookDao.books.add(createTestBook("b1"))
        repository.updateReadingProgress("b1", chapter = 5, position = 0, progress = 0.5f)
        val updated = repository.getBookById("b1")
        assertEquals(5, updated?.currentChapter)
        assertEquals(0.5f, updated?.progress)
    }

    @Test
    fun `deleteBook removes book from dao`() = runTest {
        val book = createTestBook("del-test")
        bookDao.books.add(book)
        repository.deleteBook(book)
        assertNull(repository.getBookById("del-test"))
    }

    @Test
    fun `deleteBookById removes book by id`() = runTest {
        bookDao.books.add(createTestBook("del-by-id"))
        repository.deleteBookById("del-by-id")
        assertNull(repository.getBookById("del-by-id"))
    }

    @Test
    fun `searchBooks finds matching titles case insensitive`() = runTest {
        bookDao.books.add(createTestBook("1").copy(title = "Harry Potter"))
        bookDao.books.add(createTestBook("2").copy(title = "The Hobbit"))
        bookDao.books.add(createTestBook("3").copy(title = "Harry Potter and the Goblet"))
        val results = repository.searchBooks("harry").first()
        assertEquals(2, results.size)
        assertTrue(results.all { it.title?.contains("Harry") == true })
    }

    @Test
    fun `searchBooks returns empty for no match`() = runTest {
        bookDao.books.add(createTestBook("1").copy(title = "Test Book"))
        val results = repository.searchBooks("nonexistent").first()
        assertTrue(results.isEmpty())
    }

    @Test
    fun `addBookmark creates bookmark with correct fields`() = runTest {
        val bookmark = repository.addBookmark("book1", chapterIndex = 5, position = "100")
        assertNotNull(bookmark.id)
        assertEquals("book1", bookmark.bookId)
        assertEquals(5, bookmark.chapterIndex)
    }

    @Test
    fun `deleteBookmark removes bookmark`() = runTest {
        val bookmark = repository.addBookmark("book1", chapterIndex = 0)
        repository.deleteBookmark(bookmark)
        val results = repository.getBookmarksForBook("book1").first()
        assertTrue(results.isEmpty())
    }

    @Test
    fun `getAnnotationsForBook returns only for specified book`() = runTest {
        annotationDao.annotations.add(com.example.ireader.data.local.entity.Annotation(
            id = "a1", bookId = "book1", chapterIndex = 0, startPosition = "0", endPosition = "10",
            highlightedText = "text1", color = -1, note = null, createTime = 1000, updateTime = 1000
        ))
        annotationDao.annotations.add(com.example.ireader.data.local.entity.Annotation(
            id = "a2", bookId = "book2", chapterIndex = 1, startPosition = "0", endPosition = "5",
            highlightedText = "text2", color = -1, note = null, createTime = 2000, updateTime = 2000
        ))
        val results = repository.getAnnotationsForBook("book1").first()
        assertEquals(1, results.size)
        assertEquals("text1", results[0].highlightedText)
    }

    @Test
    fun `addAnnotation creates annotation with correct fields`() = runTest {
        val annotation: com.example.ireader.data.local.entity.Annotation = repository.addAnnotation(
            bookId = "book1", chapterIndex = 2,
            highlightedText = "selected text", note = "my note", color = -65536
        )
        assertNotNull(annotation.id)
        assertEquals("book1", annotation.bookId)
        assertEquals(2, annotation.chapterIndex)
        assertEquals("selected text", annotation.highlightedText)
        assertEquals("my note", annotation.note)
    }

    @Test
    fun `deleteAnnotation removes annotation`() = runTest {
        val annotation: com.example.ireader.data.local.entity.Annotation = repository.addAnnotation("book1", chapterIndex = 0)
        repository.deleteAnnotation(annotation)
        val results = repository.getAnnotationsForBook("book1").first()
        assertTrue(results.isEmpty())
    }

    @Test
    fun `saveSetting stores value`() = runTest {
        repository.saveSetting("reader_font_size", "18")
        val result = repository.getSetting("reader_font_size")
        assertEquals("18", result)
    }

    @Test
    fun `getSetting returns null for missing key`() = runTest {
        val result = repository.getSetting("nonexistent_key")
        assertNull(result)
    }

    @Test
    fun `saveSetting overwrites existing value`() = runTest {
        repository.saveSetting("key1", "value1")
        repository.saveSetting("key1", "value2")
        val result = repository.getSetting("key1")
        assertEquals("value2", result)
    }
}
