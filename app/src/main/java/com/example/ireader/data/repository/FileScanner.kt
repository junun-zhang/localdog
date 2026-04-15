package com.example.ireader.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.documentfile.provider.DocumentFile
import com.example.ireader.data.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import java.util.UUID

/**
 * 文件扫描器 - 支持 Storage Access Framework (SAF)
 */
object FileScanner {
    
    /**
     * 从 SAF URI 创建 Book 对象
     */
    suspend fun createBookFromUri(context: Context, uri: Uri): Book? = withContext(Dispatchers.IO) {
        try {
            val documentFile = DocumentFile.fromSingleUri(context, uri) ?: return@withContext null
            
            val fileName = documentFile.name ?: "Unknown Book"
            val fileSize = documentFile.length()
            val lastModified = documentFile.lastModified()
            
            // 提取文件扩展名
            val format = getFileFormat(fileName)
            if (format.isEmpty()) return@withContext null
            
            // 提取标题（移除扩展名）
            val title = fileName.substringBeforeLast(".")
            
            // 简单的作者信息提取 - 从文件名中尝试提取
            val author = extractAuthorFromFileName(fileName)
            
            Book(
                id = UUID.randomUUID().toString(),
                title = title,
                author = author,
                coverUri = null,
                filePath = uri.toString(),
                fileSize = fileSize,
                pageCount = 0,
                lastReadPage = 0,
                lastReadTime = lastModified,
                progress = 0,
                format = format,
                addedTime = System.currentTimeMillis()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 从文件名提取格式
     */
    private fun getFileFormat(fileName: String): String {
        return when (fileName.substringAfterLast(".", "").lowercase()) {
            "epub" -> "epub"
            "pdf" -> "pdf"
            "txt" -> "txt"
            else -> ""
        }
    }
    
    /**
     * 从文件名提取作者信息
     * 支持格式: "书名-作者.epub" 或 "书名_作者.epub"
     */
    private fun extractAuthorFromFileName(fileName: String): String {
        val nameWithoutExt = fileName.substringBeforeLast(".")
        return when {
            nameWithoutExt.contains("-") -> nameWithoutExt.substringAfterLast("-").trim()
            nameWithoutExt.contains("_") -> nameWithoutExt.substringAfterLast("_").trim()
            nameWithoutExt.contains("—") -> nameWithoutExt.substringAfterLast("—").trim()
            else -> "未知作者"
        }
    }
    
    /**
     * 获取文件大小字符串
     */
    fun getFileSizeString(fileSize: Long): String {
        return when {
            fileSize < 1024 -> "$fileSize B"
            fileSize < 1024 * 1024 -> "${fileSize / 1024} KB"
            fileSize < 1024 * 1024 * 1024 -> "${fileSize / (1024 * 1024)} MB"
            else -> "${fileSize / (1024 * 1024 * 1024)} GB"
        }
    }
}