package com.example.ireader.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Ignore
import java.util.UUID

/**
 * 高亮数据模型
 * @property id 高亮唯一标识符
 * @property bookId 关联的书籍ID
 * @property bookTitle 书籍标题（非持久化，运行时填充）
 * @property content 高亮的文本内容
 * @property page 页码（PDF/TXT）或章节（EPUB）
 * @property startPosition 起始位置（字符偏移）
 * @property endPosition 结束位置（字符偏移）
 * @property color 高亮颜色
 * @property note 笔记内容
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 */
@Entity(
    tableName = "highlights",
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
data class Highlight(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val bookId: String,
    val content: String,
    val page: Int = 0,
    val startPosition: Int = 0,
    val endPosition: Int = 0,
    val color: String = "#FFEB3B", // 默认黄色
    val note: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // 非持久化字段，用于UI显示
    @Ignore
    var bookTitle: String = ""

    fun getColorDisplayName(): String = when (color.lowercase()) {
        "#ffeb3b" -> "黄色"
        "#4caf50" -> "绿色" 
        "#2196f3" -> "蓝色"
        "#f44336" -> "红色"
        "#9c27b0" -> "紫色"
        else -> "自定义"
    }
    
    fun getHighlightTimeString(): String {
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
