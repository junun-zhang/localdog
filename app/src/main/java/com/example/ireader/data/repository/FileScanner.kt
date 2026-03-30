package com.example.ireader.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.documentfile.provider.DocumentFile
import com.example.ireader.data.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            
            Book(
                id = UUID.randomUUID().toString(),
                title = title,
                author = "", // TODO: 从文件元数据中提取作者信息
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