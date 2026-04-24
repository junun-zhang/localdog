package com.example.ireader.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.ireader.data.local.entity.Book
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.ui.bookshelf.FilePickerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

class BookScanner @Inject constructor(
    private val context: Context,
    private val bookRepository: BookRepository
) {

    /**
     * 扫描目录下所有支持的书籍文件
     */
    suspend fun scanDirectory(directory: File): List<File> {
        return withContext(Dispatchers.IO) {
            FileUtil.scanBooksInDirectory(directory)
        }
    }

    /**
     * 将扫描到的文件导入到数据库（从本地扫描，文件名就是原始名）
     */
    suspend fun importBooks(files: List<File>): Int {
        var imported = 0
        return withContext(Dispatchers.IO) {
            files.forEach { file ->
                val title = file.nameWithoutExtension
                val extension = file.extension.lowercase()
                val exists = checkBookExistsByTitleAndSize(title, file.length())
                if (!exists) {
                    bookRepository.addLocalBook(
                        filePath = file.absolutePath,
                        title = title,
                        author = null,
                        format = extension,
                        fileSize = file.length()
                    )
                    imported++
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "$title 已存在，跳过导入", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            imported
        }
    }

    /**
     * 导入从文件选择器选择的文件，保留原始文件名
     */
    suspend fun importFilesWithOriginalNames(files: List<FilePickerViewModel.SelectableFile>): Int {
        var imported = 0
        return withContext(Dispatchers.IO) {
            files.forEach { selectableFile ->
                val exists = checkBookExistsByTitleAndSize(selectableFile.originalName, selectableFile.file.length())
                if (!exists) {
                    bookRepository.addLocalBook(
                        filePath = selectableFile.file.absolutePath,
                        title = selectableFile.originalName,
                        author = null,
                        format = selectableFile.extension,
                        fileSize = selectableFile.file.length()
                    )
                    imported++
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "${selectableFile.originalName} 已存在，跳过导入", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            imported
        }
    }

    /**
     * 检查书籍是否已经导入：按书名和文件大小判断更准确，
     * 因为同一文件每次从URI拷贝都会生成新路径，按路径检测不到
     */
    suspend fun checkBookExists(filePath: String): Boolean {
        // 保留原来的按路径检测用于扫描本地文件
        val books = bookRepository.getAllBooks().first()
        return books.any { it.filePath == filePath }
    }

    /**
     * 按书名和文件大小检测重复，处理URI导入的情况
     */
    suspend fun checkBookExistsByTitleAndSize(title: String, fileSize: Long): Boolean {
        val books = bookRepository.getAllBooks().first()
        // 书名相同且文件大小相同判定为同一本书
        return books.any { it.title == title && it.fileSize == fileSize }
    }

    /**
     * 从Uri导入单个文件
     */
    suspend fun importBookFromUri(uri: Uri): Book? {
        return withContext(Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val displayName = contentResolver.query(
                    uri,
                    arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
                    null,
                    null,
                    null
                )?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (cursor.moveToFirst() && nameIndex >= 0) {
                        cursor.getString(nameIndex)
                    } else {
                        null
                    }
                } ?: uri.lastPathSegment ?: "unknown"

                val extension = displayName.substringAfterLast('.', "txt").lowercase()
                val supportedExtensions = listOf("txt", "epub", "pdf")
                if (extension !in supportedExtensions) {
                    return@withContext null
                }

                val title = displayName.substringBeforeLast('.')

                // 拷贝文件到app目录保持持久访问
                val fileName = "${UUID.randomUUID()}.$extension"
                val copiedFile = FileUtil.copyUriToCache(context, uri, fileName)
                    ?: return@withContext null

                // 检查重复：按书名和文件大小
                if (checkBookExistsByTitleAndSize(title, copiedFile.length())) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "$title 已存在，跳过导入", Toast.LENGTH_SHORT).show()
                    }
                    // 删除已拷贝的重复文件
                    copiedFile.delete()
                    return@withContext null
                }

                return@withContext bookRepository.addLocalBook(
                    filePath = copiedFile.absolutePath,
                    title = title,
                    author = null,
                    format = extension,
                    fileSize = copiedFile.length()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}