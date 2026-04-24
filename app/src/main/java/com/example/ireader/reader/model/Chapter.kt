package com.example.ireader.reader.model

/**
 * 章节数据，表示一本书的一个章节内容
 */
data class Chapter(
    val index: Int,             // 章节索引
    val title: String? = null,  // 章节标题（如果能识别出来）
    val content: String         // 章节内容
)
