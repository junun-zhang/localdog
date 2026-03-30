package com.example.ireader.ui.reader

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.folioreader.FolioReader
import com.folioreader.model.HighLight
import com.folioreader.model.ReadPosition
import com.folioreader.util.ReadLocatorListener

/**
 * EPUB 阅读器 Compose 封装
 */
@Composable
fun EPUBReader(
    context: Context,
    epubPath: String,
    onReadPositionChanged: (ReadPosition) -> Unit = {},
    onHighlightCreated: (HighLight) -> Unit = {},
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    val folioReader = remember {
        FolioReader.getFolioReader()
    }
    
    AndroidView(
        factory = { viewContext ->
            // 配置 FolioReader
            folioReader.setReadLocatorListener(object : ReadLocatorListener {
                override fun saveReadLocator(readPosition: ReadPosition?) {
                    readPosition?.let { onReadPositionChanged(it) }
                }
            })
            
            // 打开 EPUB 文件
            val epubUri = Uri.parse(epubPath)
            folioReader.openBook(viewContext, epubUri)
            
            // 返回 FolioReader 的主视图
            folioReader.container
        },
        modifier = modifier
    )
}