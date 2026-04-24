package com.example.ireader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Bookmark(
    @PrimaryKey
    val id: String,
    val bookId: String,
    val chapterIndex: Int,
    val position: String?, // 文本偏移量或CFI定位信息
    val createTime: Long
)
