package com.example.ireader.data.model

/**
 * 用户数据模型
 */
data class User(
    val id: String? = null,
    val username: String? = null,
    val email: String,
    val name: String? = null,
    val avatarUrl: String? = null,
    val token: String? = null,
    val createdAt: Long? = null,
    val lastLoginAt: Long? = null
)

/**
 * 登录请求模型
 */
data class LoginRequest(
    val email: String,
    val password: String
)

/**
 * 注册请求模型
 */
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

/**
 * 认证响应模型
 */
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val user: User
)

/**
 * 刷新令牌请求模型
 */
data class RefreshTokenRequest(
    val refreshToken: String
)