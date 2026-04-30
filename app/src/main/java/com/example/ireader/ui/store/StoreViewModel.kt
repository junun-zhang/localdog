package com.example.ireader.ui.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.remote.StoreBook
import com.example.ireader.data.repository.StoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
    val downloadState: Map<String, DownloadStatus> = emptyMap()
)

enum class DownloadStatus { DOWNLOADING, SUCCESS, ERROR }

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val repository: StoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StoreUiState())
    val state: StateFlow<StoreUiState> = _state.asStateFlow()

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

            // 同时加载更多书籍
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
}
