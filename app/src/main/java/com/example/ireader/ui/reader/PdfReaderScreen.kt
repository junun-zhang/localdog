package com.example.ireader.ui.reader

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import timber.log.Timber
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfReaderScreen(
    filePath: String?,
    title: String,
    onNavigateBack: () -> Unit,
    onSaveProgress: (page: Int, totalPages: Int) -> Unit = { _, _ -> }
) {
    var currentPage by remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Save progress on system back press
    BackHandler(enabled = true) {
        if (totalPages > 0) {
            onSaveProgress(currentPage, totalPages)
        }
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (totalPages > 0) {
                            onSaveProgress(currentPage, totalPages)
                        }
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (errorMessage != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("无法加载PDF: $errorMessage", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
                        Text("返回")
                    }
                }
            } else {
                AndroidView(
                    factory = { ctx ->
                        PDFView(ctx, null).apply {
                            layoutParams = LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    update = { pdfView ->
                        if (filePath == null) {
                            errorMessage = "文件路径为空"
                            return@AndroidView
                        }
                        val file = File(filePath)
                        if (!file.exists()) {
                            errorMessage = "文件不存在: $filePath"
                            return@AndroidView
                        }
                        pdfView.fromFile(file)
                            .defaultPage(0)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .onPageChange(object : OnPageChangeListener {
                                override fun onPageChanged(page: Int, pageCount: Int) {
                                    currentPage = page + 1
                                }
                            })
                            .onLoad(object : OnLoadCompleteListener {
                                override fun loadComplete(nbPages: Int) {
                                    totalPages = nbPages
                                    isLoading = false
                                }
                            })
                            .onError(object : OnErrorListener {
                                override fun onError(t: Throwable) {
                                    Timber.e(t, "PDF加载失败")
                                    errorMessage = t.message ?: "未知错误"
                                }
                            })
                            .onRender(object : OnRenderListener {
                                override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
                                    isLoading = false
                                }
                            })
                            .load()
                    }
                )

                if (isLoading && errorMessage == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Progress bar (top)
            if (!isLoading && errorMessage == null && totalPages > 0) {
                LinearProgressIndicator(
                    progress = { currentPage.toFloat() / totalPages.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent,
                )
            }

            // Page indicator (bottom)
            if (!isLoading && errorMessage == null && totalPages > 0) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = "$currentPage / $totalPages",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
