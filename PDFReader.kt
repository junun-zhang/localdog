package com.example.ireader.ui.reader

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import java.io.File

@Composable
fun PDFReader(
    filePath: String,
    currentPage: Int = 0,
    onPageChanged: (Int) -> Unit = {},
    onLoadComplete: () -> Unit = {}
) {
    val pdfFile = remember(filePath) { File(filePath) }
    
    AndroidView(
        factory = { context ->
            PDFView(context, null).apply {
                fromFile(pdfFile)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(currentPage)
                    .onPageChange { page, _ ->
                        onPageChanged(page)
                    }
                    .onLoad { 
                        onLoadComplete()
                    }
                    .load()
            }
        },
        update = { pdfView ->
            // 更新逻辑（如果需要）
        }
    )
}
