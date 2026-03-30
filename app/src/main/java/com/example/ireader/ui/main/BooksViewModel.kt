package com.example.ireader.ui.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    val books = bookRepository.getAllBooks()
    
    fun addBookFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                bookRepository.addBookFromUri(context, uri)
            } catch (e: Exception) {
                Timber.e(e, "Failed to add book from URI: $uri")
            }
        }
    }
}