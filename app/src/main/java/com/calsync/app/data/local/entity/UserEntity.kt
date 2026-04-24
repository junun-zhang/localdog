package com.calsync.app.data.local.entity
import androidx.room.*
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String, val nickname: String,
    val avatarUrl: String?, val phone: String?
)
