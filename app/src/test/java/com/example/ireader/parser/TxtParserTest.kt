package com.example.ireader.parser

import com.example.ireader.data.local.entity.Book
import org.junit.Test
import org.junit.Assert.*
import java.io.File
import java.io.FileWriter

class TxtParserTest {

    private val txtParser = TxtParser()

    @Test
    fun `parse empty file returns empty list`() {
        val file = File.createTempFile("test", ".txt")
        val book = Book(
            id = "test",
            title = "Test",
            author = null,
            format = "txt",
            filePath = file.absolutePath,
            coverPath = null,
            fileSize = 0,
            addTime = System.currentTimeMillis(),
            lastReadTime = 0,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = false,
            bookSource = "local"
        )
        val chapters = txtParser.parse(book)
        assertTrue(chapters.isEmpty())
        file.delete()
    }

    @Test
    fun `parse simple text without chapter titles splits by length`() {
        val file = File.createTempFile("test", ".txt")
        val content = buildString {
            repeat(100) {
                append("This is line $it. ")
            }
        }
        FileWriter(file).use { it.write(content) }
        val book = Book(
            id = "test",
            title = "Test",
            author = null,
            format = "txt",
            filePath = file.absolutePath,
            coverPath = null,
            fileSize = file.length(),
            addTime = System.currentTimeMillis(),
            lastReadTime = 0,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = false,
            bookSource = "local"
        )
        val chapters = txtParser.parse(book)
        assertTrue("Should split into multiple chapters for long text", chapters.size > 1)
        assertTrue(chapters.all { it.isNotBlank() })
        file.delete()
    }

    @Test
    fun `parse text with chapter titles correctly splits by titles`() {
        val file = File.createTempFile("test", ".txt")
        val content = """
            第一章
            这是第一章的内容。
            这是第一章的第二行。

            第二章
            这是第二章的内容。
            这是第二章的第二行。

            第三章
            这是第三章的内容。
        """.trimIndent()
        FileWriter(file).use { it.write(content) }
        val book = Book(
            id = "test",
            title = "Test",
            author = null,
            format = "txt",
            filePath = file.absolutePath,
            coverPath = null,
            fileSize = file.length(),
            addTime = System.currentTimeMillis(),
            lastReadTime = 0,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = false,
            bookSource = "local"
        )
        val chapters = txtParser.parse(book)
        assertEquals(3, chapters.size)
        assertTrue(chapters[0].contains("第一章"))
        assertTrue(chapters[1].contains("第二章"))
        assertTrue(chapters[2].contains("第三章"))
        file.delete()
    }

    @Test
    fun `parse non-existent file returns empty list`() {
        val book = Book(
            id = "test",
            title = "Test",
            author = null,
            format = "txt",
            filePath = "/non/existent/path.txt",
            coverPath = null,
            fileSize = 0,
            addTime = System.currentTimeMillis(),
            lastReadTime = 0,
            currentChapter = 0,
            progress = 0f,
            isDownloaded = false,
            bookSource = "local"
        )
        val chapters = txtParser.parse(book)
        assertTrue(chapters.isEmpty())
    }
}
