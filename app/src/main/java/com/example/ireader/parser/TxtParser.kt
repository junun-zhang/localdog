package com.example.ireader.parser

import android.util.Log
import com.example.ireader.data.local.entity.Book
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject

class TxtParser @Inject constructor() {

    companion object {
        /** Maximum recommended file size in bytes (50MB). Larger files may cause OOM. */
        const val MAX_FILE_SIZE = 50L * 1024 * 1024
    }

    /**
     * 解析TXT文件，按章节分割
     * 简单规则：按空行分割，或以"第.*章"作为分隔
     */
    fun parse(book: Book): List<String> {
        val filePath = book.filePath ?: return emptyList()
        val file = File(filePath)
        if (!file.exists()) {
            Log.e("TxtParser", "File not exists: $filePath")
            return emptyList()
        }

        // Warn if file is too large (may cause OOM)
        if (file.length() > MAX_FILE_SIZE) {
            val mb = file.length() / (1024 * 1024)
            Log.w("TxtParser", "Large TXT file: ${mb}MB - may cause OOM")
            // Still attempt to parse - user may have sufficient RAM
        }

        return try {
            // 尝试多种编码读取文件
            val content = readTextWithFallback(file)
            splitIntoChapters(content)
        } catch (e: Exception) {
            Log.e("TxtParser", "Failed to parse TXT file", e)
            emptyList()
        }
    }

    /**
     * 尝试多种编码读取文件，优先 UTF-8，失败尝试 GBK，再不行用默认
     */
    private fun readTextWithFallback(file: File): String {
        return try {
            // First try UTF-8
            readFileWithEncoding(file, "UTF-8")
        } catch (e: Exception) {
            try {
                // Fallback to GBK (common in Chinese txt files)
                readFileWithEncoding(file, "GBK")
            } catch (e2: Exception) {
                // Last resort: use system default encoding
                Log.w("TxtParser", "Falling back to default encoding", e2)
                file.readText()
            }
        }
    }

    private fun readFileWithEncoding(file: File, encoding: String): String {
        return FileInputStream(file).use { fis ->
            InputStreamReader(fis, encoding).use { isr ->
                isr.readText()
            }
        }
    }

    private fun splitIntoChapters(content: String): List<String> {
        // 先按章节标题分割（锚定到行首，避免匹配正文中的章节关键词）
        val chapterPattern = Regex("^\\s*(第[零一二三四五六七八九十百千0-9]+[章回卷篇].*)$", RegexOption.MULTILINE)
        val chapters = mutableListOf<String>()

        // 如果找到章节标题，用它分割
        if (chapterPattern.containsMatchIn(content)) {
            val matches = chapterPattern.findAll(content).toList()
            val matchStarts = matches.map { it.range.first }

            for (i in matchStarts.indices) {
                val chapterStart = matchStarts[i]
                val chapterEnd = if (i + 1 < matchStarts.size) {
                    matchStarts[i + 1]
                } else {
                    content.length
                }
                val chapterContent = content.substring(chapterStart, chapterEnd).trim()
                if (chapterContent.isNotEmpty()) {
                    chapters.add(chapterContent)
                }
            }
        } else {
            // 没有明显章节标题，按大约5000字符分页
            return splitByLength(content, 1000)
        }

        return if (chapters.isEmpty()) {
            listOf(content)
        } else {
            chapters.filter { it.isNotBlank() }
        }
    }

    private fun splitByLength(content: String, maxLength: Int): List<String> {
        val lines = content.lines()
        val result = mutableListOf<String>()
        val current = StringBuilder()

        lines.forEach { line ->
            if (line.length > maxLength) {
                // Long line: flush current buffer first, then split the line
                if (current.isNotEmpty()) {
                    result.add(current.toString().trim())
                    current.clear()
                }
                var start = 0
                while (start < line.length) {
                    val end = minOf(start + maxLength, line.length)
                    result.add(line.substring(start, end).trim())
                    start = end
                }
            } else if (current.length + line.length > maxLength && current.isNotEmpty()) {
                result.add(current.toString().trim())
                current.clear()
                current.append(line).append('\n')
            } else {
                current.append(line).append('\n')
            }
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result.filter { it.isNotBlank() }
    }
}