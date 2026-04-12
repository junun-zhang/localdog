package com.example.ireader.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.database.BookmarkDao
import com.example.ireader.data.model.Bookmark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : ViewModel() {
    
    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()
    
    init { loadBookmarks() }
    
    fun loadBookmarks() {
        viewModelScope.launch {
            _bookmarks.value = emptyList()
        }
    }
    
    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkDao.deleteBookmark(bookmark.id)
            val current = _bookmarks.value
            _bookmarks.value = current.filter { it.id != bookmark.id }
        }
    }
}
