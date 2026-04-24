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
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("expiresIn") val expiresIn: Long,
    @SerializedName("user") val user: UserDto
)
data class UserDto(
    @SerializedName("id") val id: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?
)
