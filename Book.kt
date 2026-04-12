package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val coverUrl: String? = null,
    val filePath: String,
    val fileType: String = "epub",
    val addedAt: Long = System.currentTimeMillis(),
    val lastReadAt: Long = 0L,
    val readProgress: Float = 0f,
    val isFavorite: Boolean = false
) {
    fun isFree(): Boolean = true
}
