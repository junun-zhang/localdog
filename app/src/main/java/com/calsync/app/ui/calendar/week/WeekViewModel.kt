package com.calsync.app.ui.calendar.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.repository.EventRepository
import com.calsync.app.domain.model.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class WeekDay(
    val dayOfMonth: Int,
    val month: Int,
    val year: Int,
    val dayName: String,
    val isToday: Boolean,
    val timestamp: Long,
    val hasEvents: Boolean = false
)

data class WeekViewState(
    val weekStartDate: Long = getWeekStart(Calendar.getInstance()),
    val days: List<WeekDay> = emptyList()
) {
    companion object {
        fun getWeekStart(cal: Calendar): Long {
            val c = cal.clone() as Calendar
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c.timeInMillis
        }
    }
}

@HiltViewModel
class WeekViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WeekViewState())
    val state: StateFlow<WeekViewState> = _state.asStateFlow()

    init {
        loadWeek(_state.value.weekStartDate)
    }

    fun loadWeek(weekStart: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = weekStart
        val now = Calendar.getInstance()
        val dayNames = listOf("\u5468\u65e5", "\u5468\u4e00", "\u5468\u4e8c", "\u5468\u4e09", "\u5468\u56db", "\u5468\u4e94", "\u5468\u516d")
        val days = mutableListOf<WeekDay>()

        for (i in 0..6) {
            val c = cal.clone() as Calendar
            c.add(Calendar.DAY_OF_YEAR, i)
            val isToday = c.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    c.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
            days.add(
                WeekDay(
                    dayOfMonth = c.get(Calendar.DAY_OF_MONTH),
                    month = c.get(Calendar.MONTH) + 1,
                    year = c.get(Calendar.YEAR),
                    dayName = dayNames[c.get(Calendar.DAY_OF_WEEK) - 1],
                    isToday = isToday,
                    timestamp = c.timeInMillis
                )
            )
        }

        _state.update { it.copy(weekStartDate = weekStart, days = days) }
        loadEventsForWeek(weekStart)
    }

    private fun loadEventsForWeek(weekStart: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = weekStart
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startTime = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 7)
        cal.add(Calendar.MILLISECOND, -1)
        val endTime = cal.timeInMillis

        viewModelScope.launch {
            eventRepository.getEventsInRange("default", startTime, endTime)
                .collect { events ->
                    val eventDates = events.map { e ->
                        val c = Calendar.getInstance().apply { timeInMillis = e.startTime }
                        c.set(Calendar.HOUR_OF_DAY, 0); c.set(Calendar.MINUTE, 0)
                        c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0)
                        c.timeInMillis
                    }.toSet()
                    val updatedDays = _state.value.days.map { d ->
                        d.copy(hasEvents = eventDates.contains(d.timestamp))
                    }
                    _state.update { it.copy(days = updatedDays) }
                }
        }
    }

    fun goToPreviousWeek() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _state.value.weekStartDate
        cal.add(Calendar.DAY_OF_YEAR, -7)
        loadWeek(cal.timeInMillis)
    }

    fun goToNextWeek() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _state.value.weekStartDate
        cal.add(Calendar.DAY_OF_YEAR, 7)
        loadWeek(cal.timeInMillis)
    }

    fun goToToday() {
        loadWeek(WeekViewState.getWeekStart(Calendar.getInstance()))
    }
}
