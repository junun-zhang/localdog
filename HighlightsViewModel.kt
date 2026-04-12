package com.example.ireader.ui.highlights

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Highlight
import kotlinx.coroutines.launch

class HighlightsViewModel : ViewModel() {
    private val _highlights = MutableLiveData<List<Highlight>>()
    val highlights: LiveData<List<Highlight>> = _highlights

    init { loadHighlights() }

    fun loadHighlights() {
        viewModelScope.launch { _highlights.value = emptyList() }
    }

    fun deleteHighlight(highlight: Highlight) {
        viewModelScope.launch {
            val current = _highlights.value ?: emptyList()
            _highlights.value = current.filter { it.id != highlight.id }
        }
    }
}
