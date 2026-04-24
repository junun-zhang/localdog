package com.calsync.app.data.remote.model
data class HolidayDto(
    val date: Long, val name: String, val type: String,
    val lunarDate: String?, val isAdjustment: Boolean, val solarTerm: String?
)
