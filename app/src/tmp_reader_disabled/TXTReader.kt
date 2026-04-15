package com.example.ireader.ui.reader

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable
fun TXTReader(
    filePath: String,
    onScroll: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    // 监听滚动位置变化
    LaunchedEffect(scrollState.value) {
        onScroll(scrollState.value)
    }

    LaunchedEffect(filePath) {
        isLoading = true
        error = null
        try {
            content = readFileContent(context, filePath)
        } catch (e: Exception) {
            error = "无法读取文件: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("加载中...")
        }
    } else if (error != null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("错误: $error", color = MaterialTheme.colorScheme.error)
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Text(
                text = content,
                fontSize = 16.sp,
                fontFamily = FontFamily.Serif,
                lineHeight = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private suspend fun readFileContent(context: Context, filePath: String): String {
    return withContext(Dispatchers.IO) {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("文件不存在")
        }
        file.readText(Charsets.UTF_8)
    }
}