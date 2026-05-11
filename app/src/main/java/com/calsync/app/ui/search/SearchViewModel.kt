package com.calsync.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calsync.app.data.repository.EventRepository
import com.calsync.app.data.repository.TaskRepository
import com.calsync.app.domain.model.Event
import com.calsync.app.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val query: String = "",
    val isSearching: Boolean = false,
    val events: List<Event> = emptyList(),
    val tasks: List<Task> = emptyList(),
    val hasSearched: Boolean = false
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChanged(query: String) {
        _state.value = _state.value.copy(query = query)
        searchJob?.cancel()
        if (query.isBlank()) {
            _state.value = SearchUiState(query = query)
            return
        }
        searchJob = viewModelScope.launch {
            delay(300) // debounce
            _state.value = _state.value.copy(isSearching = true)
            eventRepository.searchEvents(query).collect { events ->
                _state.value = _state.value.copy(events = events, isSearching = false, hasSearched = true)
            }
        }
        viewModelScope.launch {
            delay(300)
            taskRepository.searchTasks(query).collect { tasks ->
                _state.value = _state.value.copy(tasks = tasks)
            }
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        _state.value = SearchUiState()
    }
}
