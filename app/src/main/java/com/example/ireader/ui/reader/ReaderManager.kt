package com.example.ireader.ui.reader

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.folioreader.FolioReader
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import java.io.File

/**
 * 阅读器管理器 - 根据文件格式提供不同的阅读器实现
 */
object ReaderManager {
    
    fun openEpubReader(context: Context, epubPath: String) {
        val config = FolioReader.Config()
            .setAllowedDirection(FolioReader.Direction.VERTICAL)
            .setUseNightMode(false)
            .setShowTts(false)
            
        FolioReader.getInstance().setConfig(config, true)
        FolioReader.getInstance().openBook(context, epubPath)
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
        modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
    ) {
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = modifier
        ) {
            items(txtContent.lines().size) { index ->
                androidx.compose.material3.Text(
                    text = txtContent.lines()[index],
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    modifier = androidx.compose.ui.Modifier.padding(16.dp)
                )
            }
        }
    }
}