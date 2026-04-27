package com.example.ireader.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.parser.TxtParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val txtParser: TxtParser
) : ViewModel() {

    private val bookId: String? get() = savedStateHandle.get<String>("bookId")

    private val _book = MutableStateFlow<Book?>(null)
    val book: StateFlow<Book?> = _book

    private val _chapters = MutableStateFlow<List<String>>(emptyList())
    val chapters: StateFlow<List<String>> = _chapters

    private val _currentChapter = MutableStateFlow(0)
    val currentChapter: StateFlow<Int> = _currentChapter

    private val _showMenu = MutableStateFlow(false)
    val showMenu: StateFlow<Boolean> = _showMenu

    // Track bookmarks for current book
    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks

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
                    "txt" -> parseTxt(book)
                    else -> _chapters.value = emptyList()
                }
                _currentChapter.value = book.currentChapter

                // Load bookmarks for this book
                bookRepository.getBookmarksForBook(id).collect { bmList ->
                    _bookmarks.value = bmList
                }
            }
        }
    }

    private fun parseTxt(book: Book) {
        val chapters = txtParser.parse(book)
        _chapters.value = chapters
    }

    fun isChapterBookmarked(chapterIndex: Int): Boolean {
        return _bookmarks.value.any { it.chapterIndex == chapterIndex }
    }

    fun addBookmark(bookId: String, chapterIndex: Int) {
        viewModelScope.launch {
            bookRepository.addBookmark(bookId, chapterIndex)
        }
    }

    fun removeBookmark(chapterIndex: Int) {
        viewModelScope.launch {
            val bookmark = _bookmarks.value.find { it.chapterIndex == chapterIndex }
            bookmark?.let { bookRepository.deleteBookmark(it) }
        }
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
        _showMenu.value = !_showMenu.value
    }
}
