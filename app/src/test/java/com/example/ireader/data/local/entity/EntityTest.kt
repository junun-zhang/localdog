package com.example.ireader.data.local.entity

import org.junit.Assert.*
import org.junit.Test

class EntityTest {

    @Test
    fun `Book entity can be created with all fields`() {
        val book = Book(
            id = "book-1",
            title = "Test Book",
            author = "Author",
            format = "txt",
            filePath = "/path/to/book.txt",
            coverPath = null,
            fileSize = 1024L,
            addTime = 1000L,
            lastReadTime = 2000L,
            currentChapter = 5,
            progress = 0.5f,
            isDownloaded = true,
            bookSource = "local"
        )

        assertEquals("book-1", book.id)
        assertEquals("Test Book", book.title)
        assertEquals("Author", book.author)
        assertEquals("txt", book.format)
        assertEquals(5, book.currentChapter)
        assertEquals(0.5f, book.progress, 0.001f)
        assertTrue(book.isDownloaded)
        assertEquals("local", book.bookSource)
    }

    @Test
    fun `Book entity supports null author and coverPath`() {
        val book = Book(
            id = "book-2",
            title = "No Author Book",
            author = null,
            format = "pdf",
            filePath = "/path/to/book.pdf",
            coverPath = null,
            fileSize = 2048L,
            addTime = 3000L,
            lastReadTime = 4000L,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = true,
            bookSource = "store"
        )

        assertNull(book.author)
        assertNull(book.coverPath)
        assertEquals("store", book.bookSource)
    }

    @Test
    fun `Book copy works for progress update`() {
        val book = Book(
            id = "book-3",
            title = "Progress Book",
            author = null,
            format = "txt",
            filePath = "/path/to/book.txt",
            coverPath = null,
            fileSize = 512L,
            addTime = 1000L,
            lastReadTime = 2000L,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = true,
            bookSource = "local"
        )

        val updated = book.copy(
            currentChapter = 3,
            progress = 0.3f,
            lastReadTime = 5000L
        )

        assertEquals(3, updated.currentChapter)
        assertEquals(0.3f, updated.progress, 0.001f)
        assertEquals(5000L, updated.lastReadTime)
        assertEquals(0, book.currentChapter)
        assertEquals(0f, book.progress, 0.001f)
    }

    @Test
    fun `Bookmark entity creation`() {
        val bookmark = Bookmark(
            id = "bm-1",
            bookId = "book-1",
            chapterIndex = 2,
            position = "500",
            createTime = System.currentTimeMillis()
        )

        assertEquals("bm-1", bookmark.id)
        assertEquals("book-1", bookmark.bookId)
        assertEquals(2, bookmark.chapterIndex)
        assertEquals("500", bookmark.position)
        assertNotNull(bookmark.createTime)
    }

    @Test
    fun `Annotation entity creation`() {
        val annotation = Annotation(
            id = "ann-1",
            bookId = "book-1",
            chapterIndex = 1,
            startPosition = "100",
            endPosition = "200",
            highlightedText = "Highlighted text",
            color = 0xFFFFFF00.toInt(),
            note = "Good point",
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )

        assertEquals("ann-1", annotation.id)
        assertEquals(0xFFFFFF00.toInt(), annotation.color)
        assertEquals(1, annotation.chapterIndex)
        assertEquals("100", annotation.startPosition)
        assertEquals("Highlighted text", annotation.highlightedText)
    }

    @Test
    fun `ReadingHistory entity creation`() {
        val history = ReadingHistory(
            id = 1,
            bookId = "book-1",
            readStart = System.currentTimeMillis(),
            readEnd = System.currentTimeMillis() + 60000,
            duration = 60
        )

        assertEquals(1, history.id)
        assertEquals("book-1", history.bookId)
        assertEquals(60, history.duration)
    }

    @Test
    fun `UserSettings entity creation`() {
        val settings = UserSettings(
            key = "font_size",
            value = "16"
        )

        assertEquals("font_size", settings.key)
        assertEquals("16", settings.value)
    }
}
