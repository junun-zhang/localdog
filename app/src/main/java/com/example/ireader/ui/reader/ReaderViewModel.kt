package com.example.ireader.ui.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.repository.BookRepository

data class SimpleBook(
    val id: String = "",
    val title: String = "",
    val author: String = "",
    val filePath: String = "",
    val format: String = "",
    val progress: Int = 0,
    val lastReadPage: Int = 0
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
            SimpleBook(it.id, it.title, it.author, it.filePath, it.format, it.progress, it.lastReadPage)
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
}