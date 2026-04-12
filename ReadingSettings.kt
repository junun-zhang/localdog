package com.example.ireader.data.model

data class ReadingSettings(
    val fontSize: Float = 16f,
    val lineSpacing: Float = 1.5f,
    val theme: String = "light",
    val fontFamily: String = "default",
    val margin: Int = 16,
    val brightness: Float = 1.0f
)
