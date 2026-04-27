package com.example.ireader.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.local.entity.Annotation
import com.example.ireader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    fun getBookmarks(bookId: String): StateFlow<List<Bookmark>> {
        return bookRepository.getBookmarksForBook(bookId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getAnnotations(bookId: String): StateFlow<List<Annotation>> {
        return bookRepository.getAnnotationsForBook(bookId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun addBookmark(bookId: String, chapterIndex: Int) {
        viewModelScope.launch {
            bookRepository.addBookmark(bookId, chapterIndex)
        }
    }

    fun deleteBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookRepository.deleteBookmark(bookmark)
        }
    }

    fun addNote(bookId: String, chapterIndex: Int, text: String) {
        viewModelScope.launch {
            bookRepository.addAnnotation(bookId, chapterIndex, highlightedText = text)
        }
    }

    fun deleteAnnotation(annotation: Annotation) {
        viewModelScope.launch {
            bookRepository.deleteAnnotation(annotation)
        }
    }
}
