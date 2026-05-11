package com.calsync.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("password") val password: String
)

data class LoginRequest(
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String
)

data class AuthResponse(
    @SerializedName("token") val token: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatarUrl") val avatarUrl: String?
)

data class RefreshTokenRequest(
    @SerializedName("refreshToken") val refreshToken: String
)
