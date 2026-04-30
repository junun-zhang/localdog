package com.example.ireader.ui.store

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.remote.StoreBook
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import javax.inject.Inject

data class StoreUiState(
    val loading: Boolean = true,
    val featured: List<StoreBook> = emptyList(),
    val books: List<StoreBook> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String? = null,
    val searchQuery: String = "",
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = true,
    val loadingMore: Boolean = false,
    val downloadMessages: Map<String, String> = emptyMap()
)

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: StoreRepository,
    private val bookRepository: BookRepository,
    private val app: Application,
    private val okHttpClient: OkHttpClient
) : ViewModel() {

    private val _state = MutableStateFlow(StoreUiState())
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000"
    }

    init {
        loadInitial()
    }

    fun loadInitial() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            val categoriesResult = repository.getCategories()
            val featuredResult = repository.getFeatured(10)

            featuredResult.onSuccess { featured ->
                _state.value = _state.value.copy(
                    featured = featured,
                    categories = categoriesResult.getOrDefault(emptyList()),
                    loading = false
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    loading = false,
                    error = "加载失败: ${e.localizedMessage}"
                )
            }

            loadBooks(reset = true)
        }
    }

    fun selectCategory(category: String?) {
        _state.value = _state.value.copy(selectedCategory = category, page = 1, hasMore = true)
        loadBooks(reset = true)
    }

    fun search(query: String) {
        _state.value = _state.value.copy(searchQuery = query, page = 1, hasMore = true)
        loadBooks(reset = true)
    }

    fun loadMore() {
        if (_state.value.loadingMore || !_state.value.hasMore) return
        _state.value = _state.value.copy(loadingMore = true)
        loadBooks(reset = false)
    }

    private fun loadBooks(reset: Boolean) {
        viewModelScope.launch {
            val page = if (reset) 1 else _state.value.page + 1
            val result = repository.getBooks(
                category = _state.value.selectedCategory,
                search = _state.value.searchQuery.ifBlank { null },
                page = page
            )
            result.onSuccess { response ->
                val newBooks = if (reset) response.books else _state.value.books + response.books
                _state.value = _state.value.copy(
                    books = newBooks,
                    page = page,
                    hasMore = page < (response.pagination?.pages ?: 1),
                    loadingMore = false,
                    loading = false
                )
            }.onFailure { e ->
                _state.value = _state.value.copy(
                    loading = false,
                    loadingMore = false,
                    error = "加载失败: ${e.localizedMessage}"
                )
            }
        }
    }

    fun downloadBook(book: StoreBook) {
        val bookId = book.id
        // Prevent duplicate downloads
        if (_state.value.downloadMessages.containsKey(bookId)) return

        _state.value = _state.value.copy(
            downloadMessages = _state.value.downloadMessages + (bookId to "下载中...")
        )

        viewModelScope.launch {
            try {
                // 1. Call download API (increments download count on server)
                repository.downloadBook(bookId)

                // 2. Download file from server
                val fileUrl = "$BASE_URL${book.file}"
                val result = withContext(Dispatchers.IO) {
                    downloadFile(fileUrl, book)
                }

                result.onSuccess { file ->
                    // 3. Import to local library
                    bookRepository.addLocalBook(
                        filePath = file.absolutePath,
                        title = book.title,
                        author = book.author,
                        format = book.format,
                        fileSize = file.length()
                    )
                    _state.value = _state.value.copy(
                        downloadMessages = _state.value.downloadMessages + (bookId to "导入成功 ✓")
                    )
                }.onFailure { e ->
                    _state.value = _state.value.copy(
                        downloadMessages = _state.value.downloadMessages + (bookId to "失败: ${e.localizedMessage}")
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    downloadMessages = _state.value.downloadMessages + (bookId to "失败: ${e.localizedMessage}")
                )
            }
        }
    }

    private suspend fun downloadFile(url: String, book: StoreBook): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder().url(url).build()
                val response = okHttpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    return@withContext Result.failure(Exception("下载失败: HTTP ${response.code}"))
                }

                val body = response.body ?: return@withContext Result.failure(Exception("响应为空"))
                val bytes = body.bytes()

                val ext = when (book.format) {
                    "epub" -> "epub"
                    "pdf" -> "pdf"
                    else -> "txt"
                }
                val fileName = "${book.title}.$ext".replace(Regex("[/\\\\:*?\"<>|]"), "_")
                val outputFile = File(app.filesDir, fileName)
                outputFile.writeBytes(bytes)

                Result.success(outputFile)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    fun dismissDownloadMessage(bookId: String) {
        _state.value = _state.value.copy(
            downloadMessages = _state.value.downloadMessages - bookId
        )
    }
}
