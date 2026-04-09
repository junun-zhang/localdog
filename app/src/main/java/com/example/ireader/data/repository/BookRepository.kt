package com.example.ireader.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.database.BookDao
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

/**
 * 书籍仓库，负责管理书籍数据的获取、存储和更新
 */
class BookRepository private constructor(context: Context) : CoroutineScope {
    
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    
    private val database: IReaderDatabase = IReaderDatabase.getDatabase(context)
    private val bookDao: BookDao = database.bookDao()
    
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books
    
    init {
        // 初始化时加载数据库中的书籍
        loadBooksFromDatabase()
    }
    
    /**
     * 扫描本地文件并添加到书架
     */
    fun scanLocalFiles() {
        launch {
            val scannedBooks = withContext(Dispatchers.IO) {
                FileScanner.scanBooks(context)
            }
            
            // 保存到数据库
            saveBooks(scannedBooks)
            
            // 更新UI
            loadBooksFromDatabase()
        }
    }
    
    /**
     * 从数据库加载书籍
     */
    private fun loadBooksFromDatabase() {
        launch {
            val booksFromDb = withContext(Dispatchers.IO) {
                bookDao.getAllBooks()
            }
            _books.postValue(booksFromDb)
        }
    }
    
    /**
     * 保存书籍到数据库
     */
    private fun saveBooks(books: List<Book>) {
        launch {
            withContext(Dispatchers.IO) {
                bookDao.insertBooks(books)
            }
        }
    }
    
    /**
     * 添加单本书籍
     */
    fun addBook(book: Book) {
        launch {
            withContext(Dispatchers.IO) {
                bookDao.insertBook(book)
            }
            loadBooksFromDatabase()
        }
    }
    
    /**
     * 删除书籍
     */
    fun deleteBook(bookId: String) {
        launch {
            withContext(Dispatchers.IO) {
                bookDao.deleteBook(bookId)
            }
            loadBooksFromDatabase()
        }
    }
    
    /**
     * 更新书籍阅读进度
     */
    fun updateBookProgress(bookId: String, progress: Int, lastReadPage: Int) {
        launch {
            withContext(Dispatchers.IO) {
                bookDao.updateBookProgress(bookId, progress, lastReadPage, System.currentTimeMillis())
            }
            loadBooksFromDatabase()
        }
    }
    
    /**
     * 清理资源
     */
    fun clear() {
        job.cancel()
    }
    
    companion object {
        @Volatile
        private var INSTANCE: BookRepository? = null
        
        fun getInstance(context: Context): BookRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = BookRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}