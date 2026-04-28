package com.example.ireader.ui.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.parser.EpubBookInfo
import com.example.ireader.parser.EpubParser
import com.example.ireader.parser.TxtParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks

    private val _isPdf = MutableStateFlow(false)
    val isPdf: StateFlow<Boolean> = _isPdf

    private val _isEpub = MutableStateFlow(false)
    val isEpub: StateFlow<Boolean> = _isEpub

    private val _epubInfo = MutableStateFlow<EpubBookInfo?>(null)
    val epubInfo: StateFlow<EpubBookInfo?> = _epubInfo

    companion object {
        private const val KEY_FONT_SIZE = "reader_font_size"
        private const val DEFAULT_FONT_SIZE = 16f
        private const val KEY_LINE_SPACING = "reader_line_spacing"
        private const val DEFAULT_LINE_SPACING = 1.5f
        private const val KEY_THEME = "reader_theme"
        private const val DEFAULT_THEME = "light"
    }

    private val _fontSize = MutableStateFlow(DEFAULT_FONT_SIZE)
    val fontSize: StateFlow<Float> = _fontSize

    private val _lineSpacing = MutableStateFlow(DEFAULT_LINE_SPACING)
    val lineSpacing: StateFlow<Float> = _lineSpacing

    private val _theme = MutableStateFlow(DEFAULT_THEME)
    val theme: StateFlow<String> = _theme

    init {
        loadBook()
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val size = bookRepository.getSetting(KEY_FONT_SIZE)?.toFloatOrNull() ?: DEFAULT_FONT_SIZE
            _fontSize.value = size
            val spacing = bookRepository.getSetting(KEY_LINE_SPACING)?.toFloatOrNull() ?: DEFAULT_LINE_SPACING
            _lineSpacing.value = spacing
            val themeMode = bookRepository.getSetting(KEY_THEME) ?: DEFAULT_THEME
            _theme.value = themeMode
        }
    }

    fun setFontSize(size: Float) {
        _fontSize.value = size
        viewModelScope.launch {
            bookRepository.saveSetting(KEY_FONT_SIZE, size.toString())
        }
    }

    fun setLineSpacing(spacing: Float) {
        _lineSpacing.value = spacing
        viewModelScope.launch {
            bookRepository.saveSetting(KEY_LINE_SPACING, spacing.toString())
        }
    }

    fun setTheme(themeMode: String) {
        _theme.value = themeMode
        viewModelScope.launch {
            bookRepository.saveSetting(KEY_THEME, themeMode)
        }
    }

    private fun loadBook() {
        val id = bookId ?: return
        viewModelScope.launch {
            val loadedBook = bookRepository.getBookById(id)
            _book.value = loadedBook
            loadedBook?.let { book ->
                when (book.format?.lowercase()) {
                    "txt" -> parseTxt(book)
                    "pdf" -> _isPdf.value = true
                    "epub" -> parseEpub(book)
                    else -> _chapters.value = emptyList()
                }
                _currentChapter.value = book.currentChapter

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

    private fun parseEpub(book: Book) {
        viewModelScope.launch {
            try {
                book.filePath?.let { path ->
                    val file = java.io.File(path)
                    if (file.exists()) {
                        val parser = EpubParser()
                        val info = parser.parse(file)
                        _epubInfo.value = info
                        _isEpub.value = true
                        info?.let { epubInfo ->
                            _chapters.value = epubInfo.chapters.map { it.title }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "EPUB解析失败")
                _isEpub.value = false
            }
        }
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
