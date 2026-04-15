package com.example.ireader.ui.reader

import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import java.io.File

/**
 * PDF 阅读器 Compose 封装
 */
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

/**
 * 自定义 PDFView 用于更好的 Compose 集成
 */
class ComposePDFView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : PDFView(context, attrs, defStyleAttr) {
    
    var onPdfLoadComplete: (() -> Unit)? = null
    var onPdfPageChanged: ((Int) -> Unit)? = null
    
    fun setOnLoadCompleteListener(listener: OnLoadCompleteListener) {
        super.setOnLoadCompleteListener(listener)
    }
    
    fun setOnPageChangeListener(listener: OnPageChangeListener) {
        super.setOnPageChangeListener(listener)
    }
}