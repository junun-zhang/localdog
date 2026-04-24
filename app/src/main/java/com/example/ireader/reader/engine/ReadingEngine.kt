package com.example.ireader.reader.engine

import com.example.ireader.data.local.entity.Book
import com.example.ireader.reader.model.Chapter
import com.example.ireader.reader.model.ReadingPosition

/**
 * 阅读引擎接口，不同格式书籍实现不同的引擎
 */
interface ReadingEngine {
    /**
     * 打开一本书，进行初始化解析
     * @return 是否成功打开
     */
    suspend fun openBook(book: Book): Boolean

    /**
     * 获取章节总数
     */
    fun getChapterCount(): Int

    /**
     * 获取指定章节
     */
    fun getChapter(index: Int): Chapter?

    /**
     * 获取当前章节
     */
    fun getCurrentChapter(): Chapter?

    /**
     * 跳转到指定章节
     */
    fun goToChapter(index: Int): Boolean

    /**
     * 翻到下一章
     * @return 是否成功（已经是最后一章返回false）
     */
    fun nextChapter(): Boolean

    /**
     * 翻到上一章
     * @return 是否成功（已经是第一章返回false）
     */
    fun previousChapter(): Boolean

    /**
     * 获取当前阅读位置
     */
    fun getCurrentPosition(): ReadingPosition

    /**
     * 跳转到指定位置
     */
    fun goToPosition(position: ReadingPosition): Boolean

    /**
     * 计算当前整体进度
     */
    fun calculateProgress(): Float

    /**
     * 关闭书籍，释放资源
     */
    fun close()
}
