package com.example.ireader.ui.highlights

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ireader.data.database.IReaderDatabase
import com.example.ireader.data.model.Highlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class HighlightsViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    
    private val database = IReaderDatabase.getDatabase(application)
    private val highlightDao = database.highlightDao()
    private val bookDao = database.bookDao()
    
    private val _highlights = MutableLiveData<List<Highlight>>()
    val highlights: LiveData<List<Highlight>> = _highlights
    
    init {
        loadHighlights()
    }
    
    fun loadHighlights() {
        launch {
            val highlightsList = withContext(Dispatchers.IO) {
                highlightDao.getAllHighlights().collect { highlights ->
                    // Enrich highlights with book titles
                    highlights.map { highlight ->
                        val book = bookDao.getBookById(highlight.bookId)
                        highlight.copy(
                            // Store book title in transient field if needed
                        )
                    }
                    highlights
                }
            }
        }
        // Simple load for now
        launch {
            withContext(Dispatchers.IO) {
                highlightDao.getAllHighlights().collect { highlights ->
                    _highlights.postValue(highlights)
                }
            }
        }
    }
    
    fun deleteHighlight(highlightId: String) {
        launch {
            withContext(Dispatchers.IO) {
                highlightDao.deleteHighlightById(highlightId)
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
