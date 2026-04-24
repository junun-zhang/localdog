package com.example.ireader.parser

import android.util.Log
import com.example.ireader.data.local.entity.Book
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject

class TxtParser @Inject constructor() {

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
        // 先按章节标题分割
        val chapterPattern = Regex("(第[零一二三四五六七八九十百千0-9]+[章回卷篇].*?)\\n", RegexOption.MULTILINE)
        val chapters = mutableListOf<String>()

        // 如果找到章节标题，用它分割
        if (chapterPattern.containsMatchIn(content)) {
            var lastIndex = 0
            chapterPattern.findAll(content).forEach { match ->
                val start = match.range.first
                if (start > lastIndex) {
                    val preface = content.substring(lastIndex, start).trim()
                    if (preface.isNotEmpty()) {
                        chapters.add(preface)
                    }
                }
                val chapterTitle = match.groupValues[1].trim()
                lastIndex = match.range.last + 1
            }
            // 处理最后一部分
            val last = content.substring(lastIndex).trim()
            if (last.isNotEmpty()) {
                chapters.add(last)
            }
        } else {
            // 没有明显章节标题，按大约5000字符分页
            return splitByLength(content, 5000)
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
            if (current.length + line.length > maxLength && current.isNotEmpty()) {
                result.add(current.toString().trim())
                current.clear()
            }
            current.append(line).append('\n')
        }

        if (current.isNotEmpty()) {
            result.add(current.toString().trim())
        }

        return result.filter { it.isNotBlank() }
    }
}