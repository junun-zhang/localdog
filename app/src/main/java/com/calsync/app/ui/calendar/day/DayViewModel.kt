package com.calsync.app.ui.calendar.day

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*
import javax.inject.Inject

data class DayViewState(
    val selectedDate: Long = System.currentTimeMillis()
)

@HiltViewModel
class DayViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(DayViewState())
    val state: StateFlow<DayViewState> = _state.asStateFlow()

    fun setDate(date: Long) {
        _state.update { it.copy(selectedDate = date) }
    }

    fun goToPreviousDay() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _state.value.selectedDate
        cal.add(Calendar.DAY_OF_YEAR, -1)
        _state.update { it.copy(selectedDate = cal.timeInMillis) }
    }

    fun goToNextDay() {
        val cal = Calendar.getInstance()
        cal.timeInMillis = _state.value.selectedDate
        cal.add(Calendar.DAY_OF_YEAR, 1)
        _state.update { it.copy(selectedDate = cal.timeInMillis) }
    }

    fun goToToday() {
        _state.update { it.copy(selectedDate = System.currentTimeMillis()) }
    }
}
