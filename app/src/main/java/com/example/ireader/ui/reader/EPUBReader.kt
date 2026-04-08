package com.example.ireader.ui.reader

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * EPUB 阅读器 Compose 封装
 * 
 * TODO: FolioReader 库已不可用，后续替换为 Readium
 * 临时存根实现
 */
@Composable
fun EPUBReader(
    filePath: String,
    onProgressUpdate: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // 临时存根 - FolioReader 已不可用
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "EPUB 阅读功能暂不可用\n正在寻找替代方案...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}