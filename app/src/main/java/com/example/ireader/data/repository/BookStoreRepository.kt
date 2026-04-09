package com.example.ireader.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.model.BookStoreItem
import com.example.ireader.data.model.Resource
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
    
    private val _books = MutableLiveData<Resource<List<BookStoreItem>>>()
    val books: LiveData<Resource<List<BookStoreItem>>> = _books
    
    /**
     * 从服务端获取书籍列表
     */
    suspend fun fetchBooks(category: String? = null, query: String? = null) {
        _books.value = Resource.loading(null)
        
        try {
            val response = withContext(Dispatchers.IO) {
                if (query != null) {
                    bookStoreApi.searchBooks(query)
                } else {
                    bookStoreApi.getBooks(category = category)
                }
            }
            
            if (response.isSuccessful && response.body() != null) {
                _books.value = Resource.success(response.body()!!)
            } else {
                _books.value = Resource.error(response.message() ?: "请求失败")
            }
            
        } catch (e: HttpException) {
            _books.value = Resource.error("网络请求失败: ${e.message()}")
        } catch (e: IOException) {
            _books.value = Resource.error("网络连接失败，请检查网络")
        } catch (e: Exception) {
            _books.value = Resource.error("未知错误: ${e.message}")
        }
    }
    
    /**
     * 获取书籍详情
     */
    suspend fun fetchBookDetails(bookId: String): Resource<BookStoreItem> {
        return try {
            val response = withContext(Dispatchers.IO) {
                bookStoreApi.getBookDetail(bookId)
            }
            
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message() ?: "请求失败")
            }
        } catch (e: HttpException) {
            Resource.error("网络请求失败: ${e.message()}")
        } catch (e: IOException) {
            Resource.error("网络连接失败，请检查网络")
        } catch (e: Exception) {
            Resource.error("未知错误: ${e.message}")
        }
    }
    
    /**
     * 获取书籍分类
     */
    suspend fun fetchCategories(): Resource<List<String>> {
        return try {
            val response = withContext(Dispatchers.IO) {
                bookStoreApi.getCategories()
            }
            
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(response.message() ?: "请求失败")
            }
        } catch (e: HttpException) {
            Resource.error("网络请求失败: ${e.message()}")
        } catch (e: IOException) {
            Resource.error("网络连接失败，请检查网络")
        } catch (e: Exception) {
            Resource.error("未知错误: ${e.message}")
        }
    }
    
    /**
     * 下载书籍文件
     */
    suspend fun downloadBook(bookId: String): Resource<String> {
        // 这里应该实现实际的文件下载逻辑
        // 返回下载后的本地文件路径
        return Resource.success("")
    }
}