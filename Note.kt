package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String,
    val bookId: String,
    val bookTitle: String,
    val content: String,
    val page: Int = 0,
    val chapter: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
