package com.example.ireader.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FileUtilTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var testDir: File

    @Before
    fun setup() {
        testDir = tempFolder.newFolder("test_books")
    }

    @Test
    fun `scan directory finds txt files`() {
        File(testDir, "book1.txt").createNewFile()
        File(testDir, "book2.txt").createNewFile()
        File(testDir, "notes.doc").createNewFile()

        val books = FileUtil.scanBooksInDirectory(testDir)

        assertEquals("Should find 2 txt files", 2, books.size)
        assertTrue("Should contain book1.txt", books.any { it.name == "book1.txt" })
    }

    @Test
    fun `scan directory finds epub and pdf files`() {
        File(testDir, "book.epub").createNewFile()
        File(testDir, "doc.pdf").createNewFile()
        File(testDir, "image.jpg").createNewFile()

        val books = FileUtil.scanBooksInDirectory(testDir)

        assertEquals("Should find 2 books", 2, books.size)
    }

    @Test
    fun `scan directory ignores unsupported formats`() {
        File(testDir, "readme.md").createNewFile()
        File(testDir, "data.csv").createNewFile()
        File(testDir, "photo.png").createNewFile()

        val books = FileUtil.scanBooksInDirectory(testDir)

        assertTrue("Should find no books", books.isEmpty())
    }

    @Test
    fun `scan empty directory returns empty list`() {
        val books = FileUtil.scanBooksInDirectory(testDir)
        assertTrue("Should return empty list", books.isEmpty())
    }

    @Test
    fun `scan non-existent directory returns empty list`() {
        val books = FileUtil.scanBooksInDirectory(File("/nonexistent/path"))
        assertTrue("Should return empty list", books.isEmpty())
    }

    @Test
    fun `scan subdirectories recursively`() {
        val subDir = File(testDir, "sub")
        subDir.mkdirs()
        File(testDir, "book1.txt").createNewFile()
        File(subDir, "book2.txt").createNewFile()

        val books = FileUtil.scanBooksInDirectory(testDir)

        assertEquals("Should find 2 txt files across dirs", 2, books.size)
    }

    @Test
    fun `scan is case insensitive for extensions`() {
        File(testDir, "book.TXT").createNewFile()
        File(testDir, "doc.PDF").createNewFile()
        File(testDir, "read.EPUB").createNewFile()

        val books = FileUtil.scanBooksInDirectory(testDir)

        assertEquals("Should find 3 books (case insensitive)", 3, books.size)
    }
}
