package com.example.ireader.ui.reader

import android.content.Context
import androidx.core.content.edit

// 阅读主题枚举
enum class ReadingTheme {
    LIGHT, DARK, SEPIA, GREEN
}

// 阅读设置数据类
data class ReadingPreferences(
    val theme: ReadingTheme = ReadingTheme.LIGHT,
    val fontSize: Int = 16
)

// 阅读设置管理类
class ReadingSettingsManager(private val context: Context) {
    private val prefs: android.content.SharedPreferences = context.getSharedPreferences("reading_settings", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_THEME = "reading_theme"
        private const val KEY_FONT_SIZE = "font_size"
        private const val DEFAULT_FONT_SIZE = 16
    }
    
    fun saveReadingPreferences(preferences: ReadingPreferences) {
        prefs.edit {
            putString(KEY_THEME, preferences.theme.name)
            putInt(KEY_FONT_SIZE, preferences.fontSize)
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
        
        return ReadingPreferences(theme, fontSize)
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
}