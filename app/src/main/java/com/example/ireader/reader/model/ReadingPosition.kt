package com.example.ireader.reader.model

/**
 * 阅读位置，表示当前读到哪一章哪一个位置
 */
data class ReadingPosition(
    val chapterIndex: Int,      // 当前章节索引
    val offset: Int = 0,        // 章节内偏移量（对于分页渲染可以不用，对于长文本滚动需要）
    val progress: Float = 0f    // 整体进度 0.0 ~ 1.0
)
