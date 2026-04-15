package com.example.ireader.ui.main

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {
    
    val books = bookRepository.books
    
    fun addBookFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                bookRepository.addBookFromUri(uri)
            } catch (e: Exception) {
                Timber.e(e, "Failed to add book from URI: $uri")
            }
        }
    }
}
