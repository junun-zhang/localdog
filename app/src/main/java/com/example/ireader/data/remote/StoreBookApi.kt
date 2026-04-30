package com.example.ireader.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreBookApi {

    @GET("books/featured")
    suspend fun getFeatured(@Query("limit") limit: Int = 10): ApiResponse<BooksResponse>

    @GET("books/popular")
    suspend fun getPopular(@Query("limit") limit: Int = 10): ApiResponse<BooksResponse>

    @GET("books")
    suspend fun getBooks(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): ApiResponse<BooksResponse>

    @GET("books/{id}")
    suspend fun getBookDetail(@Path("id") id: String): ApiResponse<SingleBookResponse>

    @GET("books/{id}/download")
    suspend fun downloadBook(@Path("id") id: String): ApiResponse<SingleBookResponse>

    @GET("categories")
    suspend fun getCategories(): ApiResponse<CategoriesResponse>
}
