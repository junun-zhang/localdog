package com.example.ireader.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.data.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init { loadNotes() }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = emptyList()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            val current = _notes.value
            _notes.value = current.filter { it.id != note.id }
        }
    }
}
