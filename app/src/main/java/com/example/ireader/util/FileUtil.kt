package com.example.ireader.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object FileUtil {

    /**
     * 从Uri获取文件拷贝到app缓存目录
     */
    fun copyUriToCache(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val cacheDir = context.cacheDir
                val outputFile = File(cacheDir, fileName)
                val outputStream = FileOutputStream(outputFile)
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
                outputStream.close()
                inputStream.close()
                outputFile
            }
        } catch (e: Exception) {
            Log.e("FileUtil", "copyUriToCache failed", e)
            null
        }
    }

    /**
     * 扫描指定目录下的书籍文件
     */
    fun scanBooksInDirectory(directory: File): List<File> {
        val books = mutableListOf<File>()
        val supportedExtensions = listOf("txt", "epub", "pdf")

        if (directory.exists()) {
            directory.listFiles()?.forEach { file ->
                if (file.isDirectory) {
                    books.addAll(scanBooksInDirectory(file))
                } else {
                    val extension = file.extension.lowercase()
                    if (extension in supportedExtensions) {
                        books.add(file)
                    }
                }
            }
        }
        return books
    }

    /**
     * 获取外部存储根目录
     */
    fun getExternalStorageRoot(): File? {
        return Environment.getExternalStorageDirectory()
    }
}
