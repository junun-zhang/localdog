package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 书签数据模型
 * @property id 书签唯一标识符
 * @property bookId 关联的书籍ID
 * @property title 书签标题
 * @property page 页码（PDF/TXT）或章节信息（EPUB）
 * @property position 具体位置（字节偏移或CSS选择器）
 * @property createdAt 创建时间
 * @property note 关联的笔记内容（可选）
 */
@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = Book::class,
            parentColumns = ["id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)
data class Bookmark(
    @PrimaryKey val id: String,
    val bookId: String,
    val title: String = "书签",
    val page: Int = 0,
    val position: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val note: String? = null
) {
    fun getCreatedAtString(): String {
        val diff = System.currentTimeMillis() - createdAt
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 604_800_000 -> "${diff / 86_400_000}天前"
            else -> "很久以前"
        }
    }
}