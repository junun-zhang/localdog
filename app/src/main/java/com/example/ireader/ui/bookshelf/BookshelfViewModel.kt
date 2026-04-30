package com.example.ireader.ui.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    val categories: StateFlow<List<String>> = bookRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val books: StateFlow<List<Book>> = combine(_searchQuery, _selectedCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        when {
            query.isNotBlank() -> bookRepository.searchBooks(query)
            category != null -> bookRepository.getBooksByCategory(category)
            else -> bookRepository.getAllBooks()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedBooks = MutableStateFlow<Set<String>>(emptySet())
    val selectedBooks: StateFlow<Set<String>> = _selectedBooks

    val isInMultiSelectMode: StateFlow<Boolean> = _selectedBooks
        .combine(books) { selected, _ -> selected.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleSelection(book: Book) {
        val current = _selectedBooks.value.toMutableSet()
        if (current.contains(book.id)) {
            current.remove(book.id)
        } else {
            current.add(book.id)
        }
        _selectedBooks.value = current
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun updateBookCategory(bookId: String, category: String?) {
        viewModelScope.launch {
            bookRepository.updateBookCategory(bookId, category)
        }
    }

    fun clearSelection() {
        _selectedBooks.value = emptySet()
    }

    fun deleteSelectedBooks() {
        viewModelScope.launch {
            val selectedIds = _selectedBooks.value
            val allBooks = books.value
            selectedIds.forEach { id ->
                allBooks.find { it.id == id }?.let { book ->
                    bookRepository.deleteBook(book)
                    book.filePath?.let { path ->
                        java.io.File(path).delete()
                    }
                }
            }
            _selectedBooks.value = emptySet()
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            bookRepository.deleteBook(book)
            book.filePath?.let { path ->
                java.io.File(path).delete()
            }
        }
    }
}
