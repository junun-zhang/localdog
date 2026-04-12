package com.example.ireader.data.model

enum class HighlightColor(val colorHex: String) {
    Yellow("#FFEB3B"),
    Green("#4CAF50"),
    Blue("#2196F3"),
    Red("#F44336"),
    Purple("#9C27B0");
    
    companion object {
        fun fromHex(hex: String): HighlightColor {
            return values().find { it.colorHex == hex } ?: Yellow
        }
    }
}
