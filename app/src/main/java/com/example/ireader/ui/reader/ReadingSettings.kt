package com.example.ireader.ui.reader

import android.content.Context
import androidx.core.content.edit

// 阅读主题枚举
enum class ReadingTheme {
    LIGHT, DARK, SEPIA, GREEN
}

// 阅读模式枚举
enum class ReadingMode {
    PAGED,    // 分页模式
    SCROLL    // 滚动模式
}

// 阅读设置数据类
data class ReadingPreferences(
    val theme: ReadingTheme = ReadingTheme.LIGHT,
    val fontSize: Int = 16,
    val readingMode: ReadingMode = ReadingMode.PAGED  // 默认分页
)

// 阅读设置管理类
class ReadingSettingsManager(private val context: Context) {
    private val prefs: android.content.SharedPreferences = context.getSharedPreferences("reading_settings", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_THEME = "reading_theme"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_READING_MODE = "reading_mode"
        private const val DEFAULT_FONT_SIZE = 16
    }
    
    fun saveReadingPreferences(preferences: ReadingPreferences) {
        prefs.edit {
            putString(KEY_THEME, preferences.theme.name)
            putInt(KEY_FONT_SIZE, preferences.fontSize)
            putString(KEY_READING_MODE, preferences.readingMode.name)
        }
    }
    
    fun loadReadingPreferences(): ReadingPreferences {
        val themeName = prefs.getString(KEY_THEME, ReadingTheme.LIGHT.name) ?: ReadingTheme.LIGHT.name
        val theme = try {
            ReadingTheme.valueOf(themeName)
        } catch (e: IllegalArgumentException) {
            ReadingTheme.LIGHT
        }
        val fontSize = prefs.getInt(KEY_FONT_SIZE, DEFAULT_FONT_SIZE)
        val modeName = prefs.getString(KEY_READING_MODE, ReadingMode.PAGED.name) ?: ReadingMode.PAGED.name
        val readingMode = try {
            ReadingMode.valueOf(modeName)
        } catch (e: IllegalArgumentException) {
            ReadingMode.PAGED
        }
        
        return ReadingPreferences(theme, fontSize, readingMode)
    }
    
    fun updateTheme(theme: ReadingTheme) {
        prefs.edit {
            putString(KEY_THEME, theme.name)
        }
    }
    
    fun updateFontSize(fontSize: Int) {
        prefs.edit {
            putInt(KEY_FONT_SIZE, fontSize)
        }
    }
    
    fun updateReadingMode(mode: ReadingMode) {
        prefs.edit {
            putString(KEY_READING_MODE, mode.name)
        }
    }
}