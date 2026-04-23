package com.example.ireader.data.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 书籍数据模型
 * @property id 书籍唯一标识符
 * @property title 书籍标题
 * @property author 作者
 * @property coverUri 封面图片URI
 * @property filePath 文件路径
 * @property fileSize 文件大小
 * @property pageCount 总页数（用于PDF/TXT）
 * @property lastReadPage 最后阅读页码
 * @property lastReadChapter 最后阅读章节（用于EPUB）
 * @property lastReadMode 阅读模式
 * @property lastScrollPosition 滚动位置
 * @property lastFontSize 字体大小
 * @property lastZoom PDF缩放比例
 * @property lastReadTime 最后阅读时间
 * @property progress 阅读进度 (0-100)
 * @property format 文件格式 (epub, pdf, txt)
 * @property addedTime 添加时间
 */
@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val author: String = "",
    val coverUri: String? = null,
    val filePath: String,
    val fileSize: Long = 0L,
    val pageCount: Int = 0,
    val lastReadPage: Int = 0,          // PDF/TXT 页码
    val lastReadChapter: Int = 0,       // EPUB 章节索引
    val lastReadMode: String = "PAGED", // 阅读模式
    val lastScrollPosition: Int = 0,    // 滚动位置
    val lastFontSize: Int = 16,         // 字体大小
    val lastZoom: Float = 1.0f,         // PDF 缩放比例
    val lastReadTime: Long = System.currentTimeMillis(), // 最后阅读时间
    val progress: Int = 0,
    val format: String,
    val addedTime: Long = System.currentTimeMillis()
) {
    // 获取文件格式的友好显示名称
    fun getFormatDisplayName(): String = when (format.lowercase()) {
        "epub" -> "EPUB"
        "pdf" -> "PDF"
        "txt" -> "TXT"
        else -> format.uppercase()
    }
    
    // 获取最后阅读时间的友好显示
    fun getLastReadTimeString(): String {
        val diff = System.currentTimeMillis() - lastReadTime
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 604_800_000 -> "${diff / 86_400_000}天前"
            else -> "很久以前"
        }
    }
    
    // 获取文件大小的友好显示
    fun getFileSizeString(): String {
        return when {
            fileSize < 1024 -> "$fileSize B"
            fileSize < 1024 * 1024 -> "${fileSize / 1024} KB"
            fileSize < 1024 * 1024 * 1024 -> "${fileSize / (1024 * 1024)} MB"
            else -> "${fileSize / (1024 * 1024 * 1024)} GB"
        }
    }
}