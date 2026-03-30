package com.example.ireader.data.network

import android.content.Context
import androidx.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * 认证拦截器，自动在请求头中添加 JWT token
 */
class AuthInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        
        // 从 SharedPreferences 获取 token
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val token = sharedPreferences.getString("auth_token", null)
        
        val requestWithAuth = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        
        return chain.proceed(requestWithAuth)
    }
}