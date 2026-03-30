package com.example.ireader.data.network

import com.example.ireader.data.model.BookStoreItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 图书商城 API 接口
 */
interface BookStoreApi {
    
    /**
     * 获取书籍列表
     * @param category 分类（可选）
     * @param page 页码
     * @param pageSize 每页数量
     */
    @GET("api/books")
    suspend fun getBooks(
        @Query("category") category: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<List<BookStoreItem>>
    
    /**
     * 搜索书籍
     * @param query 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     */
    @GET("api/books/search")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<List<BookStoreItem>>
    
    /**
     * 获取书籍详情
     * @param bookId 书籍ID
     */
    @GET("api/books/{id}")
    suspend fun getBookDetail(
        @Query("id") bookId: String
    ): Response<BookStoreItem>
    
    /**
     * 获取书籍分类
     */
    @GET("api/categories")
    suspend fun getCategories(): Response<List<String>>
}