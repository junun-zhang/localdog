package com.calsync.app.domain.util
import java.util.GregorianCalendar

object LunarCalendar {
    private val MONTH_NAMES = arrayOf("正月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月")
    private val DAY_NAMES = arrayOf("初一","初二","初三","初四","初五","初六","初七","初八","初九","初十","十一","十二","十三","十四","十五","十六","十七","十八","十九","二十","廿一","廿二","廿三","廿四","廿五","廿六","廿七","廿八","廿九","三十")
    private val HEAVENLY_STEMS = arrayOf("甲","乙","丙","丁","戊","己","庚","辛","壬","癸")
    private val EARTHLY_BRANCHES = arrayOf("子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥")
    private val ZODIAC = arrayOf("鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪")
    private val LUNAR_DATA = intArrayOf(
        0x04bd8,0x04ae0,0x0a570,0x054d5,0x0d260,0x0d950,0x16554,0x056a0,0x09ad0,0x055d2,
        0x04ae0,0x0a5b6,0x0a4d0,0x0d250,0x1d255,0x0b540,0x0d6a0,0x0ada2,0x095b0,0x14977,
        0x04970,0x0a4b0,0x0b4b5,0x06a50,0x06d40,0x1ab54,0x02b60,0x09570,0x052f2,0x04970,
        0x06566,0x0d4a0,0x0ea50,0x06e95,0x05ad0,0x02b60,0x186e3,0x092e0,0x1c8d7,0x0c950,
        0x0d4a0,0x1d8a6,0x0b550,0x056a0,0x1a5b4,0x025d0,0x092d0,0x0d2b2,0x0a950,0x0b557,
        0x06ca0,0x0b550,0x15355,0x04da0,0x0a5b0,0x14573,0x052b0,0x0a9a8,0x0e950,0x06aa0,
        0x0aea6,0x0ab50,0x04b60,0x0aae4,0x0a570,0x05260,0x0f263,0x0d950,0x05b57,0x056a0,
        0x096d0,0x04dd5,0x04ad0,0x0a4d0,0x0d4d4,0x0d250,0x0d558,0x0b540,0x0b6a0,0x195a6,
        0x095b0,0x049b0,0x0a974,0x0a4b0,0x0b27a,0x06a50,0x06d40,0x0af46,0x0ab60,0x09570,
        0x04af5,0x04970,0x064b0,0x074a3,0x0ea50,0x06b58,0x05ac0,0x0ab60,0x096d5,0x092e0,
        0x0c960,0x0d954,0x0d4a0,0x0da50,0x07552,0x056a0,0x0abb7,0x025d0,0x092d0,0x0cab5,
        0x0a950,0x0b4a0,0x0baa6,0x0ad50,0x055d9,0x04ba0,0x0a5b0,0x15176,0x052b0,0x0a930,
        0x07954,0x06aa0,0x0ad50,0x05b52,0x04b60,0x0a6e6,0x0a4e0,0x0d260,0x0ea65,0x0d530,
        0x05aa0,0x076a3,0x096d0,0x04afb,0x04ad0,0x0a4d0,0x1d0b6,0x0d250,0x0d520,0x0dd45,
        0x0b5a0,0x056d0,0x055b2,0x049b0,0x0a577,0x0a4b0,0x0aa50,0x1b255,0x06d20,0x0ada0,
        0x14b63,0x09370,0x049f8,0x04970,0x064b0,0x168a6,0x0ea50,0x06b20,0x1a6c4,0x0aae0,
        0x092e0,0x0d2e3,0x0c960,0x0d557,0x0d4a0,0x0da50,0x05d55,0x056a0,0x0a6d0,0x055d4,
        0x052d0,0x0a9b8,0x0a950,0x0b4a0,0x0b6a6,0x0ad50,0x055a0,0x0aba4,0x0a5b0,0x052b0,
        0x0b273,0x06930,0x07337,0x06aa0,0x0ad50,0x14b55,0x04b60,0x0a570,0x054e4,0x0d160,
        0x0e968,0x0d520,0x0daa0,0x16aa6,0x056d0,0x04ae0,0x0a9d4,0x0a2d0,0x0d150,0x0f252,0x0d520
    )

    fun getLunarDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val info = convertToLunar(year, month, dayOfMonth)
        return if (info.isLeap) "闰\${info.monthName}\${info.dayName}" else "\${info.monthName}\${info.dayName}"
    }
    fun getLunarMonthName(year: Int, month: Int, dayOfMonth: Int): String {
        val info = convertToLunar(year, month, dayOfMonth)
        return if (info.isLeap) "闰\${info.monthName}" else info.monthName
    }
    fun getLunarDayName(year: Int, month: Int, dayOfMonth: Int): String {
        return convertToLunar(year, month, dayOfMonth).dayName
    }
    fun getZodiac(year: Int): String = ZODIAC[(year - 1900) % 12]
    fun getGanZhi(year: Int): String = "\${HEAVENLY_STEMS[(year-1900)%10]}\${EARTHLY_BRANCHES[(year-1900)%12]}"
    fun getSolarTerm(timestamp: Long): String? {
        val cal = GregorianCalendar.getInstance().apply { timeInMillis = timestamp }
        val m = cal[Calendar.MONTH] + 1; val d = cal[Calendar.DAY_OF_MONTH]
        val terms = mapOf("1-6" to "小寒","1-20" to "大寒","2-4" to "立春","2-19" to "雨水",
            "3-5" to "惊蛰","3-20" to "春分","4-4" to "清明","4-20" to "谷雨",
            "5-5" to "立夏","5-21" to "小满","6-5" to "芒种","6-21" to "夏至",
            "7-7" to "小暑","7-22" to "大暑","8-7" to "立秋","8-23" to "处暑",
            "9-7" to "白露","9-23" to "秋分","10-8" to "寒露","10-23" to "霜降",
            "11-7" to "立冬","11-22" to "小雪","12-7" to "大雪","12-22" to "冬至")
        return terms["\$m-\$d"]
    }
    private fun convertToLunar(year: Int, month: Int, dayOfMonth: Int): LunarInfo {
        val baseDate = GregorianCalendar(1900, 0, 31).timeInMillis
        val targetDate = GregorianCalendar(year, month - 1, dayOfMonth).timeInMillis
        var offset = ((targetDate - baseDate) / (24*60*60*1000)).toInt()
        var lunarYear = 1900
        while (lunarYear < 2101 && offset > 0) {
            val daysInYear = daysInLunarYear(lunarYear)
            if (offset < daysInYear) break
            offset -= daysInYear; lunarYear++
        }
        val leapMonth = leapMonth(lunarYear)
        var isLeap = false; var lunarMonth = 1
        while (lunarMonth < 13 && offset > 0) {
            val dim = if (leapMonth > 0 && lunarMonth == leapMonth + 1 && !isLeap) {
                isLeap = true; daysInLunarMonth(lunarYear, lunarMonth - 1)
            } else daysInLunarMonth(lunarYear, lunarMonth)
            if (offset < dim) break
            offset -= dim; lunarMonth++; isLeap = false
        }
        return LunarInfo(lunarYear, lunarMonth, offset + 1, isLeap,
            if (lunarMonth in 1..12) MONTH_NAMES[lunarMonth-1] else "",
            if (offset + 1 in 1..30) DAY_NAMES[offset] else "")
    }
    private fun daysInLunarYear(y: Int): Int {
        var s = 348; for (i in 0..15) s += (LUNAR_DATA[y-1900] shr (16-i) and 1)
        return s + daysInLunarMonth(y, leapMonth(y))
    }
    private fun daysInLunarMonth(y: Int, m: Int): Int = if (m !in 1..12) 0 else if (LUNAR_DATA[y-1900] and (1 shl (16-m)) != 0) 30 else 29
    private fun leapMonth(y: Int): Int = LUNAR_DATA[y-1900] and 0xf
    private data class LunarInfo(val year: Int, val month: Int, val day: Int, val isLeap: Boolean, val monthName: String, val dayName: String)
}
