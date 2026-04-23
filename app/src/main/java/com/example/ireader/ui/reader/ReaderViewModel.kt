package com.example.ireader.ui.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class SimpleBook(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val filePath: String = "",
    val format: String = "",
    val progress: Int = 0,
    val lastReadPage: Int = 0,
    val lastReadChapter: Int = 0,
    val lastReadMode: String = "PAGED",
    val lastScrollPosition: Int = 0,
    val lastFontSize: Int = 16,
    val lastZoom: Float = 1.0f
)

class ReaderViewModel(application: Application) : AndroidViewModel(application) {
    
    private val bookRepository = BookRepository.getInstance(application)
    private val _readingPreferences = MutableLiveData<ReadingPreferences>()
    val readingPreferences: LiveData<ReadingPreferences> = _readingPreferences
    
    private val settingsManager = ReadingSettingsManager(application)
    
    init {
        loadReadingPreferences()
    }
    
    fun getBook(bookId: String): SimpleBook? {
        val book = bookRepository.books.value?.find { it.id == bookId }
        return book?.let {
            SimpleBook(
                it.id, 
                it.title, 
                it.author, 
                it.filePath, 
                it.format, 
                it.progress, 
                it.lastReadPage,
                it.lastReadChapter,
                it.lastReadMode,
                it.lastScrollPosition,
                it.lastFontSize,
                it.lastZoom
            )
        }
    }
    
    fun loadReadingPreferences() {
        val preferences = settingsManager.loadReadingPreferences()
        _readingPreferences.value = preferences
    }
    
    fun updateTheme(theme: ReadingTheme) {
        settingsManager.updateTheme(theme)
        _readingPreferences.value = _readingPreferences.value?.copy(theme = theme)
    }
    
    fun updateFontSize(fontSize: Int) {
        val clampedFontSize = fontSize.coerceIn(12, 24) // 限制字体大小在12sp-24sp之间
        settingsManager.updateFontSize(clampedFontSize)
        _readingPreferences.value = _readingPreferences.value?.copy(fontSize = clampedFontSize)
    }
    
    fun increaseFontSize() {
        val currentSize = _readingPreferences.value?.fontSize ?: 16
        updateFontSize(currentSize + 1)
    }
    
    fun decreaseFontSize() {
        val currentSize = _readingPreferences.value?.fontSize ?: 16
        updateFontSize(currentSize - 1)
    }
    
    /**
     * 更新阅读进度
     */
    fun updateReadProgress(
        bookId: String,
        page: Int? = null,
        chapter: Int? = null,
        progress: Int? = null,
        readingMode: String? = null,
        scrollPosition: Int? = null,
        fontSize: Int? = null,
        zoom: Float? = null
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val book = bookRepository.getBookById(bookId)
            if (book != null) {
                val updatedBook = book.copy(
                    lastReadPage = page ?: book.lastReadPage,
                    lastReadChapter = chapter ?: book.lastReadChapter,
                    progress = progress ?: book.progress,
                    lastReadMode = readingMode ?: book.lastReadMode,
                    lastScrollPosition = scrollPosition ?: book.lastScrollPosition,
                    lastFontSize = fontSize ?: book.lastFontSize,
                    lastZoom = zoom ?: book.lastZoom,
                    lastReadTime = System.currentTimeMillis()
                )
                
                bookRepository.updateBook(updatedBook)
            }
        }
    }
    
    /**
     * 保存阅读进度
     */
    fun saveReadProgress(
        bookId: String,
        page: Int,
        chapter: Int,
        progress: Int,
        readingMode: String,
        scrollPosition: Int,
        fontSize: Int,
        zoom: Float
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val book = bookRepository.getBookById(bookId)
            if (book != null) {
                val updatedBook = book.copy(
                    lastReadPage = page,
                    lastReadChapter = chapter,
                    progress = progress,
                    lastReadMode = readingMode,
                    lastScrollPosition = scrollPosition,
                    lastFontSize = fontSize,
                    lastZoom = zoom,
                    lastReadTime = System.currentTimeMillis()
                )
                
                bookRepository.updateBook(updatedBook)
            }
        }
    }
}