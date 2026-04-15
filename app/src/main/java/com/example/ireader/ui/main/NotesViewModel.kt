package com.example.ireader.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class NotesViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    
    private val database = IReaderDatabase.getDatabase(application)
    private val noteDao = database.noteDao()
    
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes
    
    init {
        loadNotes()
    }
    
    fun loadNotes() {
        launch {
            withContext(Dispatchers.IO) {
                noteDao.getAllNotes().collect { notesList ->
                    _notes.postValue(notesList)
                }
            }
        }
    }
    
    fun deleteNote(noteId: String) {
        launch {
            withContext(Dispatchers.IO) {
                val note = _notes.value?.find { it.id == noteId }
                note?.let { noteDao.deleteNote(it) }
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
