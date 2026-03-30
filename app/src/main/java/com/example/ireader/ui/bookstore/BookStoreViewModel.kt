package com.example.ireader.ui.bookstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.BookStoreItem
import com.example.ireader.data.model.Resource
import com.example.ireader.data.repository.BookStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BookStoreUiState(
    val books: List<BookStoreItem> = emptyList(),
    val categories: List<String> = listOf("热门", "新书", "免费", "小说", "技术"),
    val loadingState: LoadingState = LoadingState.Loading,
    val error: String? = null
)

enum class LoadingState {
    Loading, Success, Error
}

@HiltViewModel
class BookStoreViewModel @Inject constructor(
    private val bookStoreRepository: BookStoreRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(BookStoreUiState())
    val uiState: StateFlow<BookStoreUiState> = _uiState.asStateFlow()
    
    init {
        loadBooks()
    }
    
    fun loadBooks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingState = LoadingState.Loading)
            
            when (val result = bookStoreRepository.fetchBooks()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        books = result.data ?: emptyList(),
                        loadingState = LoadingState.Success
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loadingState = LoadingState.Error,
                        error = result.message
                    )
                }
            }
        }
    }
    
    fun loadBooksByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingState = LoadingState.Loading)
            
            when (val result = bookStoreRepository.fetchBooks(category = category)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        books = result.data ?: emptyList(),
                        loadingState = LoadingState.Success
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loadingState = LoadingState.Error,
                        error = result.message
                    )
                }
            }
        }
    }
    
    fun searchBooks(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingState = LoadingState.Loading)
            
            when (val result = bookStoreRepository.fetchBooks(query = query)) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        books = result.data ?: emptyList(),
                        loadingState = LoadingState.Success
                    )
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loadingState = LoadingState.Error,
                        error = result.message
                    )
                }
            }
        }
    }
    
    fun refreshBooks() {
        loadBooks()
    }
}