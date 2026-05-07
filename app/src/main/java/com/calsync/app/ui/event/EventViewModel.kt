package com.calsync.app.ui.event

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.repository.EventRepository
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.model.Event.Reminder
import com.calsync.app.domain.util.RecurrenceRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class EventUiState(
    val events: List<Event> = emptyList(),
    val selectedDate: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val calendarId: String = "default"
)

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _state = MutableStateFlow(EventUiState())
    val state: StateFlow<EventUiState> = _state.asStateFlow()

    fun loadEventsForDate(date: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = date
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfDay = cal.timeInMillis

        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val endOfDay = cal.timeInMillis

        Log.d("EventVM", "Loading events for date: $date, range: $startOfDay - $endOfDay, calendarId: ${_state.value.calendarId}")

        viewModelScope.launch {
            eventRepository.getEventsInRange(_state.value.calendarId, startOfDay, endOfDay)
                .collect { events ->
                    Log.d("EventVM", "Loaded ${events.size} events")
                    _state.update { it.copy(events = events, selectedDate = date) }
                }
        }
    }

    fun loadEventById(eventId: String, onResult: (Result<Event>) -> Unit = {}) {
        viewModelScope.launch {
            val event = eventRepository.getEventById(eventId)
            if (event != null) {
                onResult(Result.success(event))
            } else {
                onResult(Result.failure(Exception("事件不存在")))
            }
        }
    }

    fun loadEventsForMonth(year: Int, month: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val startOfMonth = cal.timeInMillis

        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val endOfMonth = cal.timeInMillis

        viewModelScope.launch {
            eventRepository.getEventsInRange(_state.value.calendarId, startOfMonth, endOfMonth)
                .collect { events ->
                    _state.update { it.copy(events = events) }
                }
        }
    }

    fun createEvent(
        title: String,
        startTime: Long,
        endTime: Long,
        isAllDay: Boolean = false,
        description: String? = null,
        location: String? = null,
        color: Int = 0,
        reminders: List<Reminder> = emptyList(),
        recurrenceRule: RecurrenceRule? = null,
        onComplete: (Result<Event>) -> Unit = {}
    ) {
        viewModelScope.launch {
            val event = Event(
                id = java.util.UUID.randomUUID().toString(),
                calendarId = _state.value.calendarId,
                title = title,
                startTime = startTime,
                endTime = endTime,
                isAllDay = isAllDay,
                description = description,
                location = location,
                color = color,
                reminders = reminders,
                recurrenceRule = recurrenceRule
            )
            Log.d("EventVM", "Creating event: ${event.title}, startTime=${event.startTime}, endTime=${event.endTime}, calendarId=${event.calendarId}")
            val result = eventRepository.createEvent(event)
            Log.d("EventVM", "Create result: ${result.isSuccess}, event id=${result.getOrNull()?.id}")
            onComplete(result)
        }
    }

    fun deleteEvent(event: Event, onComplete: (Result<Unit>) -> Unit = {}) {
        viewModelScope.launch {
            val result = eventRepository.deleteEvent(event)
            onComplete(result)
        }
    }

    fun updateEvent(event: Event, onComplete: (Result<Event>) -> Unit = {}) {
        viewModelScope.launch {
            val result = eventRepository.updateEvent(event)
            onComplete(result)
        }
    }
}
