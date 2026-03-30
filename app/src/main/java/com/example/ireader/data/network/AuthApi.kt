package com.example.ireader.data.network

import com.example.ireader.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 用户认证 API 接口
 */
interface AuthApi {
    
    /**
     * 用户登录
     */
    @POST("auth/login")
    suspend fun login(
        @Body credentials: LoginRequest
    ): Response<User>
    
    /**
     * 用户注册
     */
    @POST("auth/register")
    suspend fun register(
        @Body user: RegisterRequest
    ): Response<User>
    
    /**
     * 刷新令牌
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequest
    ): Response<AuthResponse>
}

/**
 * 登录请求数据类
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * 注册请求数据类
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

/**
 * 刷新令牌请求数据类
 */
data class RefreshTokenRequest(
    val refreshToken: String
)

/**
 * 认证响应数据类
 */
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)