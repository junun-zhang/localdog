package com.example.ireader.data.provider

import com.example.ireader.data.model.Book

/**
 * 模拟数据提供者，用于开发阶段显示示例书籍
 */
object BookDataProvider {
    
    fun getSampleBooks(): List<Book> {
        val now = System.currentTimeMillis()
        
        return listOf(
            Book(
                id = "1",
                title = "《百年孤独》",
                author = "加西亚·马尔克斯",
                coverUri = null,
                filePath = "/storage/emulated/0/Books/百年孤独.epub",
                fileSize = 2_500_000L,
                lastReadTime = now - 2 * 24 * 3600 * 1000, // 2天前
                progress = 30,
                format = "EPUB",
                addedTime = now - 5 * 24 * 3600 * 1000 // 5天前
            ),
            Book(
                id = "2", 
                title = "《三体》",
                author = "刘慈欣",
                coverUri = null,
                filePath = "/storage/emulated/0/Books/三体.pdf",
                fileSize = 8_200_000L,
                lastReadTime = now - 6 * 3600 * 1000, // 6小时前
                progress = 67,
                format = "PDF",
                addedTime = now - 3 * 24 * 3600 * 1000 // 3天前
            ),
            Book(
                id = "3",
                title = "《活着》",
                author = "余华",
                coverUri = null,
                filePath = "/storage/emulated/0/Books/活着.txt",
                fileSize = 450_000L,
                lastReadTime = now - 1 * 24 * 3600 * 1000, // 1天前
                progress = 100,
                format = "TXT",
                addedTime = now - 7 * 24 * 3600 * 1000 // 7天前
            ),
            Book(
                id = "4",
                title = "《红楼梦》",
                author = "曹雪芹",
                coverUri = null,
                filePath = "/storage/emulated/0/Books/红楼梦.epub",
                fileSize = 3_800_000L,
                lastReadTime = now - 12 * 3600 * 1000, // 12小时前
                progress = 38,
                format = "EPUB",
                addedTime = now - 10 * 24 * 3600 * 1000 // 10天前
            )
        )
    }
}