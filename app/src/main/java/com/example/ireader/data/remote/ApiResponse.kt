package com.example.ireader.data.remote

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)

data class BooksResponse(
    val books: List<StoreBook>,
    val pagination: Pagination?
)

data class Pagination(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)

data class CategoriesResponse(
    val categories: List<String>
)

data class SingleBookResponse(
    val book: StoreBook
)
