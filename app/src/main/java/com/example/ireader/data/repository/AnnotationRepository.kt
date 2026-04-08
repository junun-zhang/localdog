package com.example.ireader.data.repository

import android.content.Context
import com.example.ireader.data.database.BookmarkDao
import com.example.ireader.data.database.HighlightDao
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.database.NoteDao
import com.example.ireader.data.model.Bookmark
import com.example.ireader.data.model.Highlight
import com.example.ireader.data.model.Note
import kotlinx.coroutines.flow.first

/**
 * 注释仓库，负责管理书签、笔记和高亮
 */
class AnnotationRepository private constructor(context: Context) {
    private val database: IReaderDatabase = IReaderDatabase.getDatabase(context)
    private val bookmarkDao: BookmarkDao = database.bookmarkDao()
    private val noteDao: NoteDao = database.noteDao()
    private val highlightDao: HighlightDao = database.highlightDao()

    /**
     * 获取书籍的所有书签
     */
    suspend fun getBookmarksForBook(bookId: String): List<Bookmark> {
        return bookmarkDao.getBookmarksByBook(bookId).first()
    }

    /**
     * 添加书签
     */
    suspend fun addBookmark(bookmark: Bookmark) {
        bookmarkDao.insertBookmark(bookmark)
    }

    /**
     * 删除书签
     */
    suspend fun deleteBookmark(bookmarkId: String) {
        bookmarkDao.deleteBookmarkById(bookmarkId)
    }

    /**
     * 获取书籍的所有笔记
     */
    suspend fun getNotesForBook(bookId: String): List<Note> {
        return noteDao.getNotesByBook(bookId).first()
    }

    /**
     * 添加笔记
     */
    suspend fun addNote(note: Note) {
        noteDao.insertNote(note)
    }

    /**
     * 更新笔记
     */
    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    /**
     * 删除笔记 - 注意：当前 DAO 不支持按 ID 删除单个笔记
     * 需要先获取笔记对象，然后调用 deleteNote(note)
     */
    suspend fun deleteNote(noteId: String, bookId: String) {
        val notes = getNotesForBook(bookId)
        val noteToDelete = notes.find { it.id == noteId }
        if (noteToDelete != null) {
            noteDao.deleteNote(noteToDelete)
        }
    }

    /**
     * 获取书籍的所有高亮
     */
    suspend fun getHighlightsForBook(bookId: String): List<Highlight> {
        return highlightDao.getHighlightsByBook(bookId).first()
    }

    /**
     * 添加高亮
     */
    suspend fun addHighlight(highlight: Highlight) {
        highlightDao.insertHighlight(highlight)
    }

    /**
     * 删除高亮
     */
    suspend fun deleteHighlight(highlightId: String) {
        highlightDao.deleteHighlightById(highlightId)
    }

    companion object {
        @Volatile
        private var INSTANCE: AnnotationRepository? = null

        fun getInstance(context: Context): AnnotationRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AnnotationRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}