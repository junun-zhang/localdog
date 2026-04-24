package com.example.ireader.ui.bookshelf

import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.util.BookScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FilePickerViewModel @Inject constructor(
    private val bookScanner: BookScanner
) : ViewModel() {

    private val _foundFiles = MutableStateFlow<List<File>>(emptyList())
    val foundFiles: StateFlow<List<File>> = _foundFiles

    private val _selectedFiles = MutableStateFlow<Set<File>>(emptySet())
    val selectedFiles: StateFlow<Set<File>> = _selectedFiles

    fun toggleSelection(file: File) {
        val current = _selectedFiles.value.toMutableSet()
        if (current.contains(file)) {
            current.remove(file)
        } else {
            current.add(file)
        }
        _selectedFiles.value = current
    }

    fun scanLocalFiles() {
        viewModelScope.launch {
            val root = Environment.getExternalStorageDirectory()
            val files = bookScanner.scanDirectory(root)
            _foundFiles.value = files
            // 默认不选任何文件
            _selectedFiles.value = emptySet()
        }
    }

    fun importSelectedFiles() {
        viewModelScope.launch {
            bookScanner.importBooks(_selectedFiles.value.toList())
        }
    }

    fun importFile(uri: Uri) {
        viewModelScope.launch {
            bookScanner.importBookFromUri(uri)
        }
    }
}
