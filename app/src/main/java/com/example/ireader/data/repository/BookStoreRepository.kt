package com.example.ireader.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.model.BookStoreItem
import com.example.ireader.data.network.BookStoreApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 图书商城仓库，负责与服务端交互获取书籍信息
 */
@Singleton
class BookStoreRepository @Inject constructor(
    private val bookStoreApi: BookStoreApi
) {
    
    private val _books = MutableLiveData<List<BookStoreItem>>()
    val books: LiveData<List<BookStoreItem>> = _books
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    /**
     * 从服务端获取书籍列表
     */
    suspend fun fetchBooks(category: String? = null, query: String? = null) {
        _loading.value = true
        _error.value = null
        
        try {
            val response = withContext(Dispatchers.IO) {
                if (category != null) {
                    bookStoreApi.getBooksByCategory(category)
                } else if (query != null) {
                    bookStoreApi.searchBooks(query)
                } else {
                    bookStoreApi.getAllBooks()
                }
            }
            
            _books.value = response
            _loading.value = false
            
        } catch (e: HttpException) {
            _error.value = "网络请求失败: ${e.message()}"
            _loading.value = false
        } catch (e: IOException) {
            _error.value = "网络连接失败，请检查网络"
            _loading.value = false
        } catch (e: Exception) {
            _error.value = "未知错误: ${e.message}"
            _loading.value = false
        }
    }
    
    /**
     * 获取书籍详情
     */
    suspend fun fetchBookDetails(bookId: String): BookStoreItem? {
        return try {
            withContext(Dispatchers.IO) {
                bookStoreApi.getBookById(bookId)
            }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 下载书籍文件
     */
    suspend fun downloadBook(bookId: String): String? {
        // 这里应该实现实际的文件下载逻辑
        // 返回下载后的本地文件路径
        return null
    }
}