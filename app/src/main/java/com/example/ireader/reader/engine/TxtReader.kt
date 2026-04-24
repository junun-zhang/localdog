package com.example.ireader.reader.engine

import android.util.Log
import com.example.ireader.data.local.entity.Book
import com.example.ireader.parser.TxtParser
import com.example.ireader.reader.model.Chapter
import com.example.ireader.reader.model.ReadingPosition
import javax.inject.Inject

/**
 * TXT格式书籍阅读引擎实现
 */
class TxtReader @Inject constructor(
    private val txtParser: TxtParser
) : ReadingEngine {

    private var chapters: List<Chapter> = emptyList()
    private var currentChapterIndex: Int = 0
    private var book: Book? = null

    companion object {
        private const val TAG = "TxtReader"
    }

    override suspend fun openBook(book: Book): Boolean {
        return try {
            this.book = book
            val parsedChapters = txtParser.parse(book)
            this.chapters = parsedChapters.mapIndexed { index, content ->
                // 尝试从内容提取第一行作为标题
                val firstLine = content.lines().firstOrNull()
                val title = if (!firstLine.isNullOrBlank() && firstLine.length < 50) {
                    firstLine
                } else {
                    "第${index + 1}章"
                }
                Chapter(index, title, content)
            }
            // 恢复到上次阅读位置
            currentChapterIndex = book.currentChapter.coerceIn(0, chapters.lastIndex)
            Log.i(TAG, "Opened TXT book, total chapters: ${chapters.size}, current: $currentChapterIndex")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open TXT book", e)
            false
        }
    }

    override fun getChapterCount(): Int {
        return chapters.size
    }

    override fun getChapter(index: Int): Chapter? {
        return chapters.getOrNull(index)
    }

    override fun getCurrentChapter(): Chapter? {
        return chapters.getOrNull(currentChapterIndex)
    }

    override fun goToChapter(index: Int): Boolean {
        if (index in 0..chapters.lastIndex) {
            currentChapterIndex = index
            return true
        }
        return false
    }

    override fun nextChapter(): Boolean {
        return if (currentChapterIndex < chapters.lastIndex) {
            currentChapterIndex++
            true
        } else {
            false
        }
    }

    override fun previousChapter(): Boolean {
        return if (currentChapterIndex > 0) {
            currentChapterIndex--
            true
        } else {
            false
        }
    }

    override fun getCurrentPosition(): ReadingPosition {
        return ReadingPosition(
            chapterIndex = currentChapterIndex,
            progress = calculateProgress()
        )
    }

    override fun goToPosition(position: ReadingPosition): Boolean {
        return goToChapter(position.chapterIndex)
    }

    override fun calculateProgress(): Float {
        if (chapters.isEmpty()) return 0f
        return currentChapterIndex.toFloat() / chapters.size.toFloat()
    }

    override fun close() {
        chapters = emptyList()
        book = null
        currentChapterIndex = 0
    }
}
