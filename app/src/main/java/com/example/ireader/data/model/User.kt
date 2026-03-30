package com.example.ireader.data.model

/**
 * 用户数据模型
 */
data class User(
    val id: String,
    val username: String,
    val email: String,
    val avatarUrl: String? = null,
    val createdAt: Long,
    val lastLoginAt: Long
)

/**
 * 登录请求模型
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * 注册请求模型
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

/**
 * 认证响应模型
 */
data class AuthResponse(
    val token: String,
    val user: User
)