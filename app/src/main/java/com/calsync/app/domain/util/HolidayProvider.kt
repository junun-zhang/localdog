package com.calsync.app.domain.util

import java.util.Calendar
import java.util.GregorianCalendar

data class Holiday(
    val date: Long,
    val name: String,
    val type: HolidayType
)

enum class HolidayType {
    PUBLIC_HOLIDAY,     // 法定节假日
    MAKEUP_WORKDAY,     // 调休工作日
    TRADITIONAL_FESTIVAL, // 传统节日
    SCHOOL_HOLIDAY      // 寒暑假
}

object HolidayProvider {
    private val holidaysByYear = mapOf(
        2025 to listOf(
            // 元旦
            HolidayData(1, 1, "元旦", HolidayType.PUBLIC_HOLIDAY),
            // 春节
            HolidayData(1, 28, "除夕", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 29, "春节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 30, "初二", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 31, "初三", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 1, "初四", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 2, "初五", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 3, "初六", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 4, "初七", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 26, "调休上班", HolidayType.MAKEUP_WORKDAY),
            HolidayData(2, 8, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 清明节
            HolidayData(4, 4, "清明节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(4, 5, "清明假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(4, 6, "清明假", HolidayType.PUBLIC_HOLIDAY),
            // 劳动节
            HolidayData(5, 1, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 2, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 3, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 4, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 5, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(4, 27, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 端午节
            HolidayData(5, 31, "端午节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 1, "端午假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 2, "端午假", HolidayType.PUBLIC_HOLIDAY),
            // 中秋节+国庆节
            HolidayData(10, 1, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 2, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 3, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 4, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 5, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 6, "中秋节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 7, "国庆假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 8, "国庆假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(9, 28, "调休上班", HolidayType.MAKEUP_WORKDAY),
            HolidayData(10, 11, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 传统节日
            HolidayData(2, 12, "元宵节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(8, 29, "七夕", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(10, 29, "重阳节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(1, 7, "腊八节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(1, 22, "小年", HolidayType.TRADITIONAL_FESTIVAL),
        ),
        2026 to listOf(
            HolidayData(1, 1, "元旦", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 2, "元旦假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 3, "元旦假", HolidayType.PUBLIC_HOLIDAY),
            // 春节
            HolidayData(2, 16, "除夕", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 17, "春节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 18, "初二", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 19, "初三", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 20, "初四", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 21, "初五", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 22, "初六", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 23, "初七", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 14, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 清明节
            HolidayData(4, 5, "清明节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(4, 6, "清明假", HolidayType.PUBLIC_HOLIDAY),
            // 劳动节
            HolidayData(5, 1, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 2, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 3, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 4, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 5, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(4, 26, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 端午节
            HolidayData(6, 19, "端午节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 20, "端午假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 21, "端午假", HolidayType.PUBLIC_HOLIDAY),
            // 中秋节
            HolidayData(9, 27, "中秋节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(9, 28, "中秋假", HolidayType.PUBLIC_HOLIDAY),
            // 国庆节
            HolidayData(10, 1, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 2, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 3, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 4, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 5, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 6, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 7, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(9, 27, "调休上班", HolidayType.MAKEUP_WORKDAY),
            HolidayData(10, 10, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 传统节日
            HolidayData(3, 4, "元宵节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(8, 19, "七夕", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(10, 17, "重阳节", HolidayType.TRADITIONAL_FESTIVAL),
        ),
        2027 to listOf(
            HolidayData(1, 1, "元旦", HolidayType.PUBLIC_HOLIDAY),
            // 春节
            HolidayData(2, 5, "除夕", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 6, "春节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 7, "初二", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 8, "初三", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 9, "初四", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 10, "初五", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 11, "初六", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 12, "初七", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 14, "调休上班", HolidayType.MAKEUP_WORKDAY),
            HolidayData(2, 20, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 清明节
            HolidayData(4, 5, "清明节", HolidayType.PUBLIC_HOLIDAY),
            // 劳动节
            HolidayData(5, 1, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 2, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 3, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 4, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 5, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            // 端午节
            HolidayData(6, 10, "端午节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 11, "端午假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(6, 12, "端午假", HolidayType.PUBLIC_HOLIDAY),
            // 中秋节
            HolidayData(9, 15, "中秋节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(9, 16, "中秋假", HolidayType.PUBLIC_HOLIDAY),
            // 国庆节
            HolidayData(10, 1, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 2, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 3, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 4, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 5, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 6, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 7, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(9, 26, "调休上班", HolidayType.MAKEUP_WORKDAY),
            HolidayData(10, 9, "调休上班", HolidayType.MAKEUP_WORKDAY),
            // 传统节日
            HolidayData(2, 21, "元宵节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(8, 8, "七夕", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(10, 8, "重阳节", HolidayType.TRADITIONAL_FESTIVAL),
        ),
        2028 to listOf(
            HolidayData(1, 1, "元旦", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 2, "元旦假", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 3, "元旦假", HolidayType.PUBLIC_HOLIDAY),
            // 春节
            HolidayData(1, 25, "除夕", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 26, "春节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 27, "初二", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 28, "初三", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 29, "初四", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 30, "初五", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(1, 31, "初六", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(2, 1, "初七", HolidayType.PUBLIC_HOLIDAY),
            // 清明节
            HolidayData(4, 4, "清明节", HolidayType.PUBLIC_HOLIDAY),
            // 劳动节
            HolidayData(5, 1, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 2, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 3, "劳动节", HolidayType.PUBLIC_HOLIDAY),
            // 端午节
            HolidayData(5, 28, "端午节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(5, 29, "端午假", HolidayType.PUBLIC_HOLIDAY),
            // 中秋节
            HolidayData(10, 4, "中秋节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 5, "中秋假", HolidayType.PUBLIC_HOLIDAY),
            // 国庆节
            HolidayData(10, 1, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 2, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 3, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 4, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 5, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 6, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            HolidayData(10, 7, "国庆节", HolidayType.PUBLIC_HOLIDAY),
            // 传统节日
            HolidayData(2, 9, "元宵节", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(8, 27, "七夕", HolidayType.TRADITIONAL_FESTIVAL),
            HolidayData(10, 28, "重阳节", HolidayType.TRADITIONAL_FESTIVAL),
        ),
    )

    private data class HolidayData(val month: Int, val day: Int, val name: String, val type: HolidayType)

    /** 获取指定日期(时间戳)的节假日信息  */
    fun getHoliday(timestamp: Long): Holiday? {
        val cal = GregorianCalendar.getInstance()
        cal.timeInMillis = timestamp
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return holidaysByYear[year]?.find { it.month == month && it.day == day }?.let {
            Holiday(timestamp, it.name, it.type)
        }
    }

    /** 获取指定年月的所有节假日 */
    fun getHolidaysForMonth(year: Int, month: Int): List<Holiday> {
        val cal = GregorianCalendar.getInstance()
        return holidaysByYear[year]?.filter { it.month == month }?.map { data ->
            cal.set(year, month - 1, data.day, 0, 0, 0)
            cal.set(Calendar.MILLISECOND, 0)
            Holiday(cal.timeInMillis, data.name, data.type)
        } ?: emptyList()
    }

    /** 是否为法定节假日 */
    fun isPublicHoliday(timestamp: Long): Boolean =
        getHoliday(timestamp)?.type == HolidayType.PUBLIC_HOLIDAY

    /** 是否为调休工作日 */
    fun isMakeupWorkday(timestamp: Long): Boolean =
        getHoliday(timestamp)?.type == HolidayType.MAKEUP_WORKDAY
}
