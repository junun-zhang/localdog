package com.example.ireader.ui.reader

import android.content.ComponentCallbacks2
import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.local.entity.Annotation
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.local.entity.Bookmark
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.parser.EpubBookInfo
import com.example.ireader.parser.EpubParser
import com.example.ireader.parser.TxtParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * A single search result within a book.
 */
data class SearchResult(
    val chapterIndex: Int,
    val matchText: String,
    val contextBefore: String,
    val contextAfter: String,
    val matchStartInParagraph: Int,
    val matchEndInParagraph: Int
)

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val txtParser: TxtParser,
    @ApplicationContext private val context: Context
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

    private val _annotations = MutableStateFlow<List<Annotation>>(emptyList())
    val annotations: StateFlow<List<Annotation>> = _annotations

    private val _isPdf = MutableStateFlow(false)
    val isPdf: StateFlow<Boolean> = _isPdf

    private val _isEpub = MutableStateFlow(false)
    val isEpub: StateFlow<Boolean> = _isEpub

    private val _epubInfo = MutableStateFlow<EpubBookInfo?>(null)
    val epubInfo: StateFlow<EpubBookInfo?> = _epubInfo

    // ── Search state ─────────────────────────────────────────────────
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> = _searchResults

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private var searchJob: Job? = null

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
        registerMemoryCallback()
    }

    // ── Memory management ──
    private fun registerMemoryCallback() {
        val callback = object : ComponentCallbacks2 {
            override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {}
            override fun onLowMemory() {
                _chapters.value = emptyList()
            }
            override fun onTrimMemory(level: Int) {
                if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
                    _chapters.value = emptyList()
                }
            }
        }
        context.registerComponentCallbacks(callback)
    }

    // ── Search methods ───────────────────────────────────────────────
    fun startSearch() {
        _isSearchActive.value = true
        _searchQuery.value = ""
        _searchResults.value = emptyList()
        _showMenu.value = false
    }

    fun stopSearch() {
        _isSearchActive.value = false
        _searchQuery.value = ""
        _searchResults.value = emptyList()
    }

    fun searchInBook(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        // Cancel previous search job - withContext respects cancellation when ensureActive is called
        searchJob?.cancel()
        _isSearching.value = true
        searchJob = viewModelScope.launch {
            val results = withContext(Dispatchers.Default) {
                ensureActive()  // Check if this job was cancelled before starting
                val chapters = _chapters.value
                // Timber.d("searchInBook: query=%s, chapters=%d ...", query, chapters.size, chapters.firstOrNull()?.take(50))
                val q = query.lowercase()
                val resultList = mutableListOf<SearchResult>()
                chapters.forEachIndexed { chapterIdx, content ->
                    ensureActive()  // Check cancellation between chapters
                    val paragraphs = content.split("\n\n", "\r\n\r\n")
                    paragraphs.forEach { para ->
                        val lower = para.lowercase()
                        var startIndex = 0
                        while (true) {
                            val matchIdx = lower.indexOf(q, startIndex)
                            if (matchIdx < 0) break
                            // Build context: up to 30 chars before/after
                            val beforeStart = (matchIdx - 30).coerceAtLeast(0)
                            val afterEnd = (matchIdx + query.length + 30).coerceAtMost(para.length)
                            val contextBefore = if (beforeStart > 0) "..." else ""
                            val contextAfter = if (afterEnd < para.length) "..." else ""
                            resultList.add(
                                SearchResult(
                                    chapterIndex = chapterIdx,
                                    matchText = para.substring(matchIdx, matchIdx + query.length),
                                    contextBefore = contextBefore + para.substring(beforeStart, matchIdx),
                                    contextAfter = para.substring(matchIdx + query.length, afterEnd) + contextAfter,
                                    matchStartInParagraph = matchIdx,
                                    matchEndInParagraph = matchIdx + query.length
                                )
                            )
                            startIndex = matchIdx + query.length
                        }
                    }
                }
                resultList
            }
            // Only update state if this job is still the current search job
            if (query == _searchQuery.value) {
                _searchResults.value = results
                _isSearching.value = false
            }
        }
    }

    fun goToSearchResult(result: SearchResult) {
        changeChapter(result.chapterIndex)
    }

    // ── Existing methods ─────────────────────────────────────────────
    override fun onCleared() {
        super.onCleared()
        saveReadingProgress()
    }

    private fun saveReadingProgress() {
        val id = bookId ?: return
        val chapter = _currentChapter.value
        val total = _chapters.value.size.coerceAtLeast(1)
        val progress = chapter.toFloat() / total.toFloat()
        viewModelScope.launch {
            bookRepository.updateReadingProgress(
                bookId = id,
                chapter = chapter,
                position = 0,
                progress = progress
            )
        }
    }

    fun saveReaderProgress(bookId: String, page: Int, totalPages: Int) {
        val progress = if (totalPages > 0) page.toFloat() / totalPages.toFloat() else 0f
        viewModelScope.launch {
            bookRepository.updateReadingProgress(
                bookId = bookId,
                chapter = page - 1,
                position = 0,
                progress = progress
            )
        }
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
                bookRepository.getAnnotationsForBook(id).collect { annList ->
                    _annotations.value = annList
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
                        val parser = EpubParser(context)
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

    fun addAnnotation(bookId: String, chapterIndex: Int, text: String, note: String?, color: Int) {
        viewModelScope.launch {
            bookRepository.addAnnotation(
                bookId = bookId,
                chapterIndex = chapterIndex,
                highlightedText = text,
                note = note,
                color = color
            )
        }
    }

    fun changeChapter(chapter: Int) {
        if (chapter in 0 until (_chapters.value.size)) {
            _currentChapter.value = chapter
            saveReadingProgress()
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
