package com.example.ireader.data.network

import com.example.ireader.data.model.AuthResponse
import com.example.ireader.data.model.LoginRequest
import com.example.ireader.data.model.RegisterRequest
import com.example.ireader.data.model.RefreshTokenRequest
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
    ): Response<AuthResponse>
    
    /**
     * 用户注册
     */
    @POST("auth/register")
    suspend fun register(
        @Body user: RegisterRequest
    ): Response<AuthResponse>
    
    /**
     * 刷新令牌
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequest
    ): Response<AuthResponse>
}