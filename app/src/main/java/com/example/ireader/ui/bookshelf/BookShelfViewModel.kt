package com.example.ireader.ui.bookshelf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookShelfViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bookRepository = BookRepository.getInstance(application)
    
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            bookRepository.books.observeForever { bookList ->
                _books.value = bookList
            }
            _isLoading.value = false
        }
    }
    
    fun deleteBook(bookId: String) {
        bookRepository.deleteBook(bookId)
    }
}