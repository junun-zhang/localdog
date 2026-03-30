package com.example.ireader.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.documentfile.provider.DocumentFile
import com.example.ireader.data.model.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat

/**
 * 文件扫描器，用于扫描设备上的电子书文件
 */
class FileScanner(private val context: Context) {
    
    companion object {
        private const val TAG = "FileScanner"
        private val SUPPORTED_FORMATS = setOf("epub", "pdf", "txt")
    }
    
    /**
     * 扫描设备上的电子书文件
     * @return 扫描到的书籍列表
     */
    suspend fun scanBooks(): List<Book> = withContext(Dispatchers.IO) {
        val books = mutableListOf<Book>()
        
        try {
            // 扫描外部存储中的书籍文件
            scanExternalStorage(books)
            
            Log.d(TAG, "Found ${books.size} books")
        } catch (e: Exception) {
            Log.e(TAG, "Error scanning books", e)
        }
        
        books
    }
    
    /**
     * 扫描外部存储中的书籍文件
     */
    private fun scanExternalStorage(books: MutableList<Book>) {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_MODIFIED
        )
        
        val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?)"
        val selectionArgs = arrayOf("application/epub+zip", "application/pdf", "text/plain")
        
        context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            val dataColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)
            val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED)
            
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn).toString()
                val name = cursor.getString(nameColumn)
                val size = cursor.getLong(sizeColumn)
                val path = cursor.getString(dataColumn)
                val lastModified = cursor.getLong(dateModifiedColumn) * 1000L // Convert to milliseconds
                
                // 提取文件扩展名
                val extension = getFileExtension(name)
                if (extension !in SUPPORTED_FORMATS) continue
                
                // 创建书籍对象
                val book = Book(
                    id = id,
                    title = getBookTitleFromFileName(name),
                    author = "", // TODO: 从文件元数据中提取作者信息
                    coverUri = null,
                    filePath = path,
                    fileSize = size,
                    lastReadTime = lastModified,
                    progress = 0,
                    format = extension.uppercase(),
                    addedTime = System.currentTimeMillis()
                )
                
                books.add(book)
            }
        }
    }
    
    /**
     * 从文件名中提取书籍标题
     */
    private fun getBookTitleFromFileName(fileName: String): String {
        // 移除文件扩展名
        val title = fileName.substringBeforeLast('.')
        // 移除可能的作者信息（简单的启发式方法）
        return title.split(" - ").firstOrNull() ?: title
    }
    
    /**
     * 获取文件扩展名
     */
    private fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast('.', "").lowercase()
    }
    
    /**
     * 格式化文件大小为人类可读的字符串
     */
    fun formatFileSize(bytes: Long): String {
        val df = DecimalFormat("#.##")
        return when {
            bytes >= 1073741824 -> df.format(bytes / 1073741824.0) + " GB"
            bytes >= 1048576 -> df.format(bytes / 1048576.0) + " MB"
            bytes >= 1024 -> df.format(bytes / 1024.0) + " KB"
            else -> "$bytes B"
        }
    }
}