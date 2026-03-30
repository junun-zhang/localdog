package com.example.ireader.ui.main

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.net.toUri

/**
 * 文件选择器封装，用于 Android 11+ 的 Storage Access Framework
 */
@Composable
fun rememberFilePicker(
    onFilePicked: (Uri) -> Unit,
    mimeType: String = "*/*"
): FilePicker {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { onFilePicked(it) }
        }
    )
    
    return remember {
        FilePicker { launcher.launch(arrayOf(mimeType)) }
    }
}

class FilePicker(
    private val launchPicker: () -> Unit
) {
    fun pickFile(mimeType: String = "*/*") {
        launchPicker()
    }
}