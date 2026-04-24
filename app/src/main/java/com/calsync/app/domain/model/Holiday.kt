package com.calsync.app.domain.model
data class Holiday(
    val date: Long,
    val name: String,
    val type: HolidayType,
    val lunarDate: String? = null,
    val isAdjustment: Boolean = false,
    val solarTerm: String? = null
) {
    enum class HolidayType { HOLIDAY, WORKDAY, FESTIVAL, SOLAR_TERM, SCHOOL_HOLIDAY }
}
