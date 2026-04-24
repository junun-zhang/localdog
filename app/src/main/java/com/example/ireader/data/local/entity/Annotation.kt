package com.example.ireader.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "annotations",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Annotation(
    @PrimaryKey
    val id: String,
    val bookId: String,
    val chapterIndex: Int,
    val startPosition: String,
    val endPosition: String,
    val highlightedText: String?,
    val color: Int, // 高亮颜色
    val note: String?, // 批注文字
    val createTime: Long,
    val updateTime: Long
)
