package com.example.ireader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String?,
    val format: String?, // "txt", "epub", "pdf"
    val filePath: String?,
    val coverPath: String?,
    val fileSize: Long?,
    val addTime: Long,
    val lastReadTime: Long,
    val currentChapter: Int,
    val progress: Float, // 0.0 ~ 1.0
    val isDownloaded: Boolean,
    val bookSource: String, // "local", "store"
    val category: String? = null,
    val tags: String? = null
)
