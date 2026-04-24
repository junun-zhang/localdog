package com.example.ireader.data.local.entity

import org.junit.Test
import org.junit.Assert.*
import java.util.*

class BookTest {

    @Test
    fun () {
        val book = Book(
            id = test-id,
            title = Test Book,
            author = Test Author,
            filePath = /test/path.txt,
            addTime = Date()
        )
        
        assertEquals(test-id, book.id)
        assertEquals(Test Book, book.title)
        assertEquals(Test Author, book.author)
        assertEquals(/test/path.txt, book.filePath)
        assertEquals(0f, book.progress)
        assertEquals(0, book.totalPages)
        assertEquals(0, book.currentPage)
        assertNull(book.coverPath)
        assertNotNull(book.addTime)
    }

    @Test
    fun () {
        val book = Book(
            id = test-id,
            title = Test Book,
            filePath = /test/path.txt
        ).copy(totalPages = 100, currentPage = 50)
        
        assertEquals(0.5f, book.progress)
        assertEquals(100, book.totalPages)
        assertEquals(50, book.currentPage)
    }
}
