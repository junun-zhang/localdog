package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 笔记数据模型
 * @property id 笔记唯一标识符
 * @property bookId 关联的书籍ID
 * @property content 笔记内容
 * @property position 位置信息（页码或章节）
 * @property chapterName 章节名称（EPUB）
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey val id: String,
    val bookId: String,
    val content: String,
    val position: Int = 0, // 页码（PDF/TXT）或章节索引（EPUB）
    val chapterName: String = "", // EPUB章节名称
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
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