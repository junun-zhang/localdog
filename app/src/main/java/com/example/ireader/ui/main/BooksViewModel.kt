package com.example.ireader.ui.main

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.repository.BookRepository
import kotlinx.coroutines.launch

class BooksViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bookRepository = BookRepository.getInstance(application)
    val books = bookRepository.books
    
    fun addBookFromUri(uri: Uri) {
        viewModelScope.launch {
            try {
                bookRepository.addBookFromUri(uri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}