package com.calsync.app.data.local.entity
import androidx.room.*
@Entity(tableName = "calendars")
data class CalendarEntity(
    @PrimaryKey val id: String, val name: String, val color: Int,
    val isVisible: Boolean, val isShared: Boolean, val ownerUserId: String,
    val role: Int, val inviteCode: String?
)
