package com.example.ireader.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.parser.TxtParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val txtParser: TxtParser
) : ViewModel() {

    private val bookId: String? = savedStateHandle.get<String>(bookId)

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _chapters = MutableStateFlow<List<String>>(emptyList())
    val chapters: StateFlow<List<String>> = _chapters

    private val _currentChapter = MutableStateFlow(0)
    val currentChapter: StateFlow<Int> = _currentChapter

    init {
        loadBook()
    }

    private fun loadBook() {
        val id = bookId ?: return
        viewModelScope.launch {
            val loadedBook = bookRepository.getBookById(id)
            _book.value = loadedBook
            loadedBook?.let { book ->
                when (book.format?.lowercase()) {
                    txt -> parseTxt(book)
                    // TODO: epub pdf 后续支持
                    else -> _chapters.value = emptyList()
                }
                _currentChapter.value = book.currentChapter
            }
        }
    }

    private fun parseTxt(book: Book) {
        val chapters = txtParser.parse(book)
        _chapters.value = chapters
    }

    fun changeChapter(chapter: Int) {
        if (chapter in 0 until (_chapters.value.size)) {
            _currentChapter.value = chapter
            _book.value?.let { book ->
                viewModelScope.launch {
                    bookRepository.updateReadingProgress(
                        bookId = book.id,
                        chapter = chapter,
                        position = 0,
                        progress = chapter.toFloat() / _chapters.value.size.coerceAtLeast(1)
                    )
                }
            }
        }
    }

    fun nextChapter(): Boolean {
        if (_currentChapter.value < _chapters.value.lastIndex) {
            changeChapter(_currentChapter.value + 1)
            return true
        }
        return false
    }

    fun prevChapter(): Boolean {
        if (_currentChapter.value > 0) {
            changeChapter(_currentChapter.value - 1)
            return true
        }
        return false
    }

    fun toggleMenu() {
        // TODO: Implement menu toggle
    }
}
