package com.calsync.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class CalendarDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: Int,
    @SerializedName("ownerId") val ownerId: String,
    @SerializedName("inviteCode") val inviteCode: String?,
    @SerializedName("createdAt") val createdAt: String
)

data class CreateCalendarRequest(
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: Int
)

data class MemberDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("role") val role: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("joinedAt") val joinedAt: String
)

data class InviteRequest(
    @SerializedName("userId") val userId: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("role") val role: String
)

data class JoinRequest(
    @SerializedName("inviteCode") val inviteCode: String
)
