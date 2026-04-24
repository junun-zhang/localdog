package com.example.ireader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reading_history",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ReadingHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val bookId: String,
    val readStart: Long,
    val readEnd: Long,
    val duration: Int // 阅读时长(秒)
)
