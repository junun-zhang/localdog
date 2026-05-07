package com.calsync.app.ui.calendar.month

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.repository.EventRepository
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.util.LunarCalendar
import com.calsync.app.domain.util.HolidayProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class CalendarDay(
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
    val isToday: Boolean,
    val isCurrentMonth: Boolean,
    val lunarDay: String,
    val lunarMonth: String,
    val holidayName: String?,
    val solarTerm: String?,
    val hasEvents: Boolean,
    val timestamp: Long
)

data class MonthViewState(
    val currentMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val currentYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    val days: List<CalendarDay> = emptyList(),
    val selectedDate: Long? = null,
    val weekStartsOnMonday: Boolean = true
)

@HiltViewModel
class MonthViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MonthViewState())
    val state: StateFlow<MonthViewState> = _state.asStateFlow()

    private val calendar = Calendar.getInstance()
    private val eventsCache = mutableMapOf<Long, Boolean>()

    init {
        loadMonth(_state.value.currentYear, _state.value.currentMonth)
    }

    fun loadMonth(year: Int, month: Int) {
        calendar.set(year, month - 1, 1)
        val days = generateCalendarDays(year, month)
        _state.update { it.copy(currentYear = year, currentMonth = month, days = days) }
        loadEventsForMonth(year, month)
    }

    fun goToPreviousMonth() {
        val s = _state.value
        val newMonth = if (s.currentMonth == 1) 12 else s.currentMonth - 1
        val newYear = if (s.currentMonth == 1) s.currentYear - 1 else s.currentYear
        loadMonth(newYear, newMonth)
    }

    fun goToNextMonth() {
        val s = _state.value
        val newMonth = if (s.currentMonth == 12) 1 else s.currentMonth + 1
        val newYear = if (s.currentMonth == 12) s.currentYear + 1 else s.currentYear
        loadMonth(newYear, newMonth)
    }

    fun goToToday() {
        val now = Calendar.getInstance()
        loadMonth(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1)
    }

    fun selectDate(timestamp: Long) {
        _state.update { it.copy(selectedDate = timestamp) }
    }

    private fun loadEventsForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.set(year, month - 1, 1, 0, 0, 0)
            cal.set(Calendar.MILLISECOND, 0)
            val startTime = cal.timeInMillis

            cal.set(Calendar.MONTH, month)
            cal.add(Calendar.DAY_OF_MONTH, -1)
            cal.set(Calendar.HOUR_OF_DAY, 23)
            cal.set(Calendar.MINUTE, 59)
            cal.set(Calendar.SECOND, 59)
            cal.set(Calendar.MILLISECOND, 999)
            val endTime = cal.timeInMillis

            eventRepository.getEventsInRange("default", startTime, endTime)
                .collect { events ->
                    val midnightCals = Calendar.getInstance()
                    val eventDays = mutableSetOf<Long>()
                    for (event in events) {
                        midnightCals.timeInMillis = event.startTime
                        midnightCals.set(Calendar.HOUR_OF_DAY, 0)
                        midnightCals.set(Calendar.MINUTE, 0)
                        midnightCals.set(Calendar.SECOND, 0)
                        midnightCals.set(Calendar.MILLISECOND, 0)
                        eventDays.add(midnightCals.timeInMillis)
                    }

                    val currentDays = _state.value.days.map { day ->
                        val hasEvent = eventDays.contains(day.timestamp)
                        if (hasEvent) {
                            eventsCache[day.timestamp] = true
                        }
                        day.copy(hasEvents = hasEvent || eventsCache.containsKey(day.timestamp))
                    }
                    _state.update { it.copy(days = currentDays) }
                }
        }
    }

    private fun generateCalendarDays(year: Int, month: Int): List<CalendarDay> {
        val days = mutableListOf<CalendarDay>()
        val cal = Calendar.getInstance()

        // First day of the month
        cal.set(year, month - 1, 1)
        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Previous month days
        val prevMonth = if (month == 1) 12 else month - 1
        val prevYear = if (month == 1) year - 1 else year
        cal.set(prevYear, prevMonth - 1, 1)
        val daysInPrevMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val startOffset = if (_state.value.weekStartsOnMonday) {
            if (firstDayOfWeek == Calendar.SUNDAY) 6 else firstDayOfWeek - 2
        } else {
            firstDayOfWeek - 1
        }

        // Add previous month days
        for (i in startOffset - 1 downTo 0) {
            val day = daysInPrevMonth - i
            cal.set(prevYear, prevMonth - 1, day)
            days.add(createCalendarDay(cal, day, prevMonth, prevYear, false))
        }

        // Add current month days
        val today = Calendar.getInstance()
        for (day in 1..daysInMonth) {
            val isToday = day == today.get(Calendar.DAY_OF_MONTH) &&
                    month == today.get(Calendar.MONTH) + 1 &&
                    year == today.get(Calendar.YEAR)
            days.add(createCalendarDay(cal, day, month, year, isToday))
        }

        // Add next month days
        val totalCells = if (days.size <= 35) 35 else 42
        val nextMonth = if (month == 12) 1 else month + 1
        val nextYear = if (month == 12) year + 1 else year
        var nextDay = 1
        while (days.size < totalCells) {
            cal.set(nextYear, nextMonth - 1, nextDay)
            days.add(createCalendarDay(cal, nextDay, nextMonth, nextYear, false))
            nextDay++
        }

        return days
    }

    private fun getHolidayName(timestamp: Long): String? {
        val h = HolidayProvider.getHoliday(timestamp) ?: return null
        return if (h.type == com.calsync.app.domain.util.HolidayType.PUBLIC_HOLIDAY || 
                   h.type == com.calsync.app.domain.util.HolidayType.TRADITIONAL_FESTIVAL || h.type == com.calsync.app.domain.util.HolidayType.SCHOOL_HOLIDAY) h.name else null
    }

    private fun createCalendarDay(
        cal: Calendar,
        day: Int,
        month: Int,
        year: Int,
        isToday: Boolean
    ): CalendarDay {
        val timestamp = cal.timeInMillis
        val lunarDay = LunarCalendar.getLunarDayName(year, month, day)
        val lunarMonth = LunarCalendar.getLunarMonthName(year, month, day)
        val solarTerm = LunarCalendar.getSolarTerm(timestamp)
        val currentMonth = _state.value.currentMonth

        return CalendarDay(
            dayOfMonth = day,
            month = month,
            year = year,
            isToday = isToday,
            isCurrentMonth = month == currentMonth,
            lunarDay = lunarDay,
            lunarMonth = lunarMonth,
            holidayName = getHolidayName(timestamp),
            solarTerm = solarTerm,
            hasEvents = eventsCache.containsKey(timestamp),
            timestamp = timestamp
        )
    }
}
