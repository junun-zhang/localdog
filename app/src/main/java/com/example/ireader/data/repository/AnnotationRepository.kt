package com.example.ireader.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.database.BookmarkDao
import com.example.ireader.data.database.HighlightDao
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.database.NoteDao
import com.example.ireader.data.model.Bookmark
import com.example.ireader.data.model.Highlight
import com.example.ireader.data.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    fun getBookmarksForBook(bookId: String): LiveData<List<Bookmark>> {
        val bookmarks = MutableLiveData<List<Bookmark>>()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                bookmarkDao.getBookmarksByBookId(bookId)
            }
            bookmarks.postValue(result)
        }
        return bookmarks
    }
    
    /**
     * 添加书签
     */
    fun addBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookmarkDao.insertBookmark(bookmark)
            }
        }
    }
    
    /**
     * 删除书签
     */
    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookmarkDao.deleteBookmark(bookmarkId)
            }
        }
    }
    
    /**
     * 获取书籍的所有笔记
     */
    fun getNotesForBook(bookId: String): LiveData<List<Note>> {
        val notes = MutableLiveData<List<Note>>()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                noteDao.getNotesByBookId(bookId)
            }
            notes.postValue(result)
        }
        return notes
    }
    
    /**
     * 添加笔记
     */
    fun addNote(note: Note) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.insertNote(note)
            }
        }
    }
    
    /**
     * 更新笔记
     */
    fun updateNote(note: Note) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.updateNote(note)
            }
        }
    }
    
    /**
     * 删除笔记
     */
    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.deleteNote(noteId)
            }
        }
    }
    
    /**
     * 获取书籍的所有高亮
     */
    fun getHighlightsForBook(bookId: String): LiveData<List<Highlight>> {
        val highlights = MutableLiveData<List<Highlight>>()
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                highlightDao.getHighlightsByBookId(bookId)
            }
            highlights.postValue(result)
        }
        return highlights
    }
    
    /**
     * 添加高亮
     */
    fun addHighlight(highlight: Highlight) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                highlightDao.insertHighlight(highlight)
            }
        }
    }
    
    /**
     * 删除高亮
     */
    fun deleteHighlight(highlightId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                highlightDao.deleteHighlight(highlightId)
            }
        }
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