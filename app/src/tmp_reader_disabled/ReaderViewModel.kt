package com.example.ireader.ui.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    application: Application,
    private val bookRepository: BookRepository
) : AndroidViewModel(application) {
    
    fun updateBookProgress(bookId: String, progress: Int, lastReadPage: Int = 0) {
        viewModelScope.launch {
            bookRepository.updateBookProgress(bookId, progress, lastReadPage)
        }
    }
    
    fun getBookById(bookId: String): Book? {
        return bookRepository.getBookById(bookId)
    }
}