package com.example.ireader.ui.bookshelf

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ireader.util.BookScanner
import com.example.ireader.util.FileUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
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

    // 将Uri对应的文件拷贝到app目录并添加到列表供用户确认
    fun addFileFromUri(uri: Uri, context: Context) {
        viewModelScope.launch {
            val displayName = context.contentResolver.query(
                uri,
                arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    cursor.getString(nameIndex)
                } else {
                    null
                }
            } ?: uri.lastPathSegment ?: "unknown"

            val extension = displayName.substringAfterLast('.', "txt").lowercase()
            val supportedExtensions = listOf("txt", "epub", "pdf")
            if (extension !in supportedExtensions) {
                return@launch
            }

            // 拷贝文件到app目录
            val fileName = UUID.randomUUID().toString() + "." + extension
            val copiedFile = FileUtil.copyUriToCache(context, uri, fileName)
            copiedFile?.let {
                // 添加到已找到文件列表并自动选中
                val currentList = _foundFiles.value.toMutableList()
                if (!currentList.any { it.absolutePath == copiedFile.absolutePath }) {
                    currentList.add(copiedFile)
                    _foundFiles.value = currentList
                }
                toggleSelection(copiedFile)
            }
        }
    }
}