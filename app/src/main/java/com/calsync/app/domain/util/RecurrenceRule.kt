package com.calsync.app.domain.util
import java.util.Calendar
import java.util.GregorianCalendar

data class RecurrenceRule(
    val freq: Freq, val interval: Int = 1,
    val until: Long? = null, val count: Int? = null,
    val byDay: List<String>? = null,
    val byMonthDay: List<Int>? = null,
    val byMonth: List<Int>? = null
) {
    enum class Freq { DAILY, WEEKLY, MONTHLY, YEARLY }
    fun toRRule(): String {
        val parts = mutableListOf("FREQ=\${freq.name}")
        if (interval != 1) parts.add("INTERVAL=\$interval")
        until?.let { parts.add("UNTIL=\${formatUntil(it)}") }
        count?.let { parts.add("COUNT=\$it") }
        byDay?.let { parts.add("BYDAY=\${it.joinToString(",")}") }
        byMonthDay?.let { parts.add("BYMONTHDAY=\${it.joinToString(",")}") }
        byMonth?.let { parts.add("BYMONTH=\${it.joinToString(",")}") }
        return parts.joinToString(";")
    }
    companion object {
        fun fromRRule(rule: String?): RecurrenceRule? {
            if (rule.isNullOrBlank()) return null
            val parts = rule.split(";").associate { val kv = it.split("="); kv[0] to kv.getOrNull(1) }
            val freq = Freq.valueOf(parts["FREQ"] ?: return null)
            return RecurrenceRule(
                freq = freq,
                interval = parts["INTERVAL"]?.toIntOrNull() ?: 1,
                until = parts["UNTIL"]?.let { parseUntil(it) },
                count = parts["COUNT"]?.toIntOrNull(),
                byDay = parts["BYDAY"]?.split(","),
                byMonthDay = parts["BYMONTHDAY"]?.split(",")?.mapNotNull { it.toIntOrNull() },
                byMonth = parts["BYMONTH"]?.split(",")?.mapNotNull { it.toIntOrNull() }
            )
        }
        private fun formatUntil(ts: Long): String {
            val c = GregorianCalendar.getInstance().apply { timeInMillis = ts }
            return String.format("%04d%02d%02dT%02d%02d%02dZ", c[Calendar.YEAR], c[Calendar.MONTH]+1, c[Calendar.DAY_OF_MONTH], c[Calendar.HOUR_OF_DAY], c[Calendar.MINUTE], c[Calendar.SECOND])
        }
        private fun parseUntil(s: String): Long? = try {
            GregorianCalendar(s.substring(0,4).toInt(), s.substring(4,6).toInt()-1, s.substring(6,8).toInt(), s.substring(9,11).toInt(), s.substring(11,13).toInt(), s.substring(13,15).toInt()).timeInMillis
        } catch(e: Exception) { null }
    }
}

fun generateOccurrences(startTime: Long, rule: RecurrenceRule, maxOccurrences: Int = 365): List<Long> {
    val occurrences = mutableListOf<Long>()
    val cal = GregorianCalendar.getInstance().apply { timeInMillis = startTime }
    val endDate = rule.until?.let { GregorianCalendar().apply { timeInMillis = it } }
    repeat(rule.count ?: maxOccurrences) {
        if (endDate != null && cal.timeInMillis > endDate.timeInMillis) return@repeat
        occurrences.add(cal.timeInMillis)
        when (rule.freq) {
            RecurrenceRule.Freq.DAILY -> cal.add(Calendar.DAY_OF_MONTH, rule.interval)
            RecurrenceRule.Freq.WEEKLY -> cal.add(Calendar.WEEK_OF_YEAR, rule.interval)
            RecurrenceRule.Freq.MONTHLY -> cal.add(Calendar.MONTH, rule.interval)
            RecurrenceRule.Freq.YEARLY -> cal.add(Calendar.YEAR, rule.interval)
        }
    }
    return occurrences
}
