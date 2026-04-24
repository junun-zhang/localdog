package com.calsync.app.domain.model
import java.util.UUID
data class Calendar(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val color: Int = 0,
    val isVisible: Boolean = true,
    val isShared: Boolean = false,
    val ownerUserId: String = "",
    val role: CalendarRole = CalendarRole.OWNER,
    val inviteCode: String? = null
) {
    enum class CalendarRole { OWNER, EDITOR, VIEWER }
}
