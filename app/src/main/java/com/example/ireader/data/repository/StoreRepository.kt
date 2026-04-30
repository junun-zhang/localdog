package com.example.ireader.data.repository

import com.example.ireader.data.remote.ApiResponse
import com.example.ireader.data.remote.BooksResponse
import com.example.ireader.data.remote.CategoriesResponse
import com.example.ireader.data.remote.SingleBookResponse
import com.example.ireader.data.remote.StoreBook
import com.example.ireader.data.remote.StoreBookApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    private val api: StoreBookApi
) {
    suspend fun getFeatured(limit: Int = 10): Result<List<StoreBook>> = safeCall {
        val resp = api.getFeatured(limit)
        if (resp.success && resp.data != null) resp.data.books else emptyList()
    }

    suspend fun getPopular(limit: Int = 10): Result<List<StoreBook>> = safeCall {
        val resp = api.getPopular(limit)
        if (resp.success && resp.data != null) resp.data.books else emptyList()
    }

    suspend fun getBooks(
        category: String? = null,
        search: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Result<BooksResponse> = safeCall {
        val resp = api.getBooks(category, search, page, limit)
        if (resp.success && resp.data != null) resp.data
        else BooksResponse(emptyList(), null)
    }

    suspend fun getBookDetail(id: String): Result<StoreBook> = safeCall {
        val resp = api.getBookDetail(id)
        if (resp.success && resp.data != null) resp.data.book
        else throw Exception(resp.message ?: "获取详情失败")
    }

    suspend fun getCategories(): Result<List<String>> = safeCall {
        val resp = api.getCategories()
        if (resp.success && resp.data != null) resp.data.categories
        else emptyList()
    }
}

private suspend fun <T> safeCall(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
