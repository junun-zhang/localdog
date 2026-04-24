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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookshelfViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    val books: StateFlow<List<Book>> = bookRepository.getAllBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedBooks = MutableStateFlow<Set<String>>(emptySet())
    val selectedBooks: StateFlow<Set<String>> = _selectedBooks

    // 是否处于多选模式
    val isInMultiSelectMode: StateFlow<Boolean> = _selectedBooks
        .combine(books) { selected, _ ->
            selected.isNotEmpty()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleSelection(book: Book) {
        val current = _selectedBooks.value.toMutableSet()
        if (current.contains(book.id)) {
            current.remove(book.id)
        } else {
            current.add(book.id)
        }
        _selectedBooks.value = current
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
                    // 删除数据库记录
                    bookRepository.deleteBook(book)
                    // 删除物理文件
                    book.filePath?.let { path ->
                        java.io.File(path).delete()
                    }
                }
            }
            // 清空选择
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