package com.example.ireader.data.model

/**
 * 通用资源状态包装类
 * @param <T> 数据类型
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error<out T>(val message: String, val data: T? = null, val exception: Exception? = null) : Resource<T>()
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    
    companion object {
        fun <T> success(data: T): Resource<T> = Success(data)
        fun <T> error(message: String, data: T? = null, exception: Exception? = null): Resource<T> = Error(message, data, exception)
        fun <T> loading(data: T? = null): Resource<T> = Loading(data)
    }
    
    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Error -> data
        is Loading -> data
    }
    
    fun getOrThrow(): T {
        if (this is Success) return data
        throw IllegalStateException((this as? Error)?.message ?: "Resource is not successful")
    }
}