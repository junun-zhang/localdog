package com.example.ireader.data.repository

import android.content.Context
import android.net.Uri
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
import kotlin.coroutines.CoroutineContext

/**
 * 书籍仓库，负责管理书籍数据的获取、存储和更新
 */
class BookRepository private constructor(private val context: Context) : CoroutineScope {
    
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
     * 注意：需要通过 SAF 选择文件，调用 addBookFromUri 添加单本书籍
     */
    fun scanLocalFiles() {
        // SAF 模式下，用户需要手动选择文件
        // 此方法保留用于未来可能的自动扫描功能
        loadBooksFromDatabase()
    }
    
    /**
     * 从数据库加载书籍
     */
    private fun loadBooksFromDatabase() {
        launch {
            val booksFromDb = withContext(Dispatchers.IO) {
                bookDao.getAllBooksOnce()
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
     * 从 URI 添加书籍（SAF 模式）
     */
    suspend fun addBookFromUri(uri: Uri): Book? {
        val book = FileScanner.createBookFromUri(context, uri) ?: return null
        addBook(book)
        return book
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
     * 根据 ID 获取书籍
     */
    suspend fun getBookById(bookId: String): Book? {
        return withContext(Dispatchers.IO) {
            bookDao.getBookById(bookId)
        }
    }
    
    /**
     * 更新书籍
     */
    fun updateBook(book: Book) {
        launch {
            withContext(Dispatchers.IO) {
                bookDao.updateBook(book)
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