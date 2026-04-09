package com.example.ireader.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.model.Bookmark
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 书签 ViewModel，管理书签数据
 */
class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    
    private val database = IReaderDatabase.getDatabase(application)
    private val bookmarkDao = database.bookmarkDao()
    
    private val _allBookmarks = MutableLiveData<List<Bookmark>>()
    val allBookmarks: LiveData<List<Bookmark>> = _allBookmarks
    
    private val _bookmarksForBook = MutableLiveData<List<Bookmark>>()
    
    init {
        loadAllBookmarks()
    }
    
    /**
     * 加载所有书签
     */
    private fun loadAllBookmarks() {
        viewModelScope.launch {
            val bookmarks = bookmarkDao.getAllBookmarksList()
            _allBookmarks.postValue(bookmarks)
        }
    }
    
    /**
     * 获取指定书籍的书签
     */
    fun getBookmarksForBook(bookId: String): LiveData<List<Bookmark>> {
        viewModelScope.launch {
            val bookmarks = bookmarkDao.getBookmarksForBook(bookId)
            _bookmarksForBook.postValue(bookmarks)
        }
        return _bookmarksForBook
    }
    
    /**
     * 添加书签
     */
    fun addBookmark(bookId: String, title: String, page: Int, position: String = "", note: String? = null) {
        viewModelScope.launch {
            val bookmark = Bookmark(
                id = UUID.randomUUID().toString(),
                bookId = bookId,
                title = title,
                page = page,
                position = position,
                note = note
            )
            bookmarkDao.insertBookmark(bookmark)
            loadAllBookmarks()
        }
    }
    
    /**
     * 删除书签
     */
    fun deleteBookmark(bookmarkId: String) {
        viewModelScope.launch {
            bookmarkDao.deleteBookmark(bookmarkId)
            loadAllBookmarks()
        }
    }
    
    /**
     * 更新书签笔记
     */
    fun updateBookmarkNote(bookmarkId: String, note: String) {
        viewModelScope.launch {
            bookmarkDao.updateBookmarkNote(bookmarkId, note)
            loadAllBookmarks()
        }
    }
}