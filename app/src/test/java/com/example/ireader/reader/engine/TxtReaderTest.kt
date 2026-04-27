package com.example.ireader.reader.engine

import com.example.ireader.data.local.entity.Book
import com.example.ireader.parser.TxtParser
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.nio.charset.Charset

class TxtReaderTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var txtReader: TxtReader
    private lateinit var testBook: Book

    private fun createTestFile(content: String): File {
        val file = tempFolder.newFile("novel.txt")
        file.writeText(content, Charset.forName("UTF-8"))
        return file
    }

    private fun createBook(filePath: String): Book {
        return Book(
            id = "test-book",
            title = "Test Novel",
            author = null,
            format = "txt",
            filePath = filePath,
            coverPath = null,
            fileSize = File(filePath).length(),
            addTime = System.currentTimeMillis(),
            lastReadTime = System.currentTimeMillis(),
            currentChapter = 0,
            progress = 0f,
            isDownloaded = true,
            bookSource = "local"
        )
    }

    @Before
    fun setup() {
        val parser = TxtParser()
        txtReader = TxtReader(parser)
    }

    @Test
    fun `open book with chapters`() = runBlocking {
        val file = createTestFile("第一章 开始\n这是第一章。\n第二章 继续\n这是第二章。")
        testBook = createBook(file.absolutePath)

        val result = txtReader.openBook(testBook)

        assertTrue("Should open successfully", result)
        assertEquals(2, txtReader.getChapterCount())
    }

    @Test
    fun `navigate to specific chapter`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。\n第三章 C\n内容C。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        assertTrue(txtReader.goToChapter(1))
        assertEquals(1, txtReader.getCurrentChapter()?.index)
    }

    @Test
    fun `next chapter navigation`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        assertTrue(txtReader.nextChapter())
        assertEquals(1, txtReader.getCurrentChapter()?.index)

        // Already at last chapter
        assertFalse(txtReader.nextChapter())
    }

    @Test
    fun `previous chapter navigation`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        txtReader.goToChapter(1)

        assertTrue(txtReader.previousChapter())
        assertEquals(0, txtReader.getCurrentChapter()?.index)

        // Already at first chapter
        assertFalse(txtReader.previousChapter())
    }

    @Test
    fun `go to chapter out of bounds returns false`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        assertFalse(txtReader.goToChapter(5))
        assertFalse(txtReader.goToChapter(-1))
    }

    @Test
    fun `calculate progress`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。\n第三章 C\n内容C。\n第四章 D\n内容D。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        txtReader.goToChapter(0)
        assertEquals(0.0f, txtReader.calculateProgress(), 0.01f)

        txtReader.goToChapter(2)
        assertEquals(0.5f, txtReader.calculateProgress(), 0.01f)

        txtReader.goToChapter(3)
        assertEquals(0.75f, txtReader.calculateProgress(), 0.01f)
    }

    @Test
    fun `get current position`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)

        txtReader.goToChapter(1)
        val position = txtReader.getCurrentPosition()

        assertEquals(1, position.chapterIndex)
        assertEquals(0.5f, position.progress, 0.01f)
    }

    @Test
    fun `close resets state`() = runBlocking {
        val file = createTestFile("第一章 A\n内容A。\n第二章 B\n内容B。")
        testBook = createBook(file.absolutePath)
        txtReader.openBook(testBook)
        txtReader.goToChapter(1)

        txtReader.close()

        assertEquals(0, txtReader.getChapterCount())
        assertNull(txtReader.getCurrentChapter())
        assertEquals(0, txtReader.getCurrentPosition().chapterIndex)
    }

    @Test
    fun `open non-existent file returns false`() = runBlocking {
        testBook = createBook("/nonexistent/book.txt")

        val result = txtReader.openBook(testBook)

        assertFalse("Should fail for non-existent file", result)
        assertEquals(0, txtReader.getChapterCount())
    }
}
