package com.example.ireader.ui.reader

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

/**
 * 阅读器管理器 - 根据文件格式提供不同的阅读器实现
 */
object ReaderManager {
    
    /**
     * EPUB 阅读器（临时存根）
     * TODO: 使用 Readium 替代
     */
    @Composable
    fun EpubReaderView(
        epubPath: String,
        onPageChanged: (Int) -> Unit = {}
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "EPUB 阅读功能暂不可用",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
    
    @Composable
    fun PdfReaderView(
        pdfPath: String,
        onPageChanged: (Int) -> Unit = {},
        onLoadComplete: (Int) -> Unit = {}
    ) {
        AndroidView(
            factory = { context ->
                PDFView(context, null).apply {
                    fromFile(File(pdfPath))
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .onPageChange { page, _ ->
                            onPageChanged(page)
                        }
                        .onLoad { pages ->
                            onLoadComplete(pages)
                        }
                        .load()
                }
            }
        )
    }
    
    @Composable
    fun TxtReaderView(
        txtContent: String,
        modifier: Modifier = Modifier
    ) {
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = modifier
        ) {
            items(txtContent.lines().size) { index ->
                androidx.compose.material3.Text(
                    text = txtContent.lines()[index],
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}