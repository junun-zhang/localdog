package com.example.ireader.ui.reader

import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import timber.log.Timber
import java.io.File
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PdfReaderScreen(
    filePath: String?,
    title: String,
    onNavigateBack: () -> Unit
) {
    var currentPage by remember { mutableStateOf(1) }
    var totalPages by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    // UI state - tap to show/hide
    var showMenu by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    
    // Track view width for tap position calculation
    var viewWidth by remember { mutableIntStateOf(0) }
    var pdfViewRef by remember { mutableStateOf<PDFView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    AnimatedVisibility(
                        visible = showMenu,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置")
                        }
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (totalPages > 0) {
                    Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                        LinearProgressIndicator(
                            progress = { if (totalPages > 0) currentPage.toFloat() / totalPages else 0f },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "第 $currentPage / $totalPages 页",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
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
                // Track touch positions - use mutable state to survive recomposition
                var downX by remember { mutableFloatStateOf(0f) }
                var downY by remember { mutableFloatStateOf(0f) }
                
                AndroidView(
                    factory = { ctx ->
                        PDFView(ctx, null).apply {
                            layoutParams = LayoutParams(
                                LayoutParams.MATCH_PARENT,
                                LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInteropFilter { event ->
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    downX = event.x
                                    downY = event.y
                                }
                                MotionEvent.ACTION_UP -> {
                                    val dx = event.x - downX
                                    val dy = event.y - downY
                                    val distance = sqrt(dx * dx + dy * dy)
                                    if (distance < 15f && viewWidth > 0) {
                                        // Check if tap is in center third
                                        if (event.x > viewWidth * 0.3f && event.x < viewWidth * 0.7f) {
                                            showMenu = !showMenu
                                        }
                                    }
                                }
                            }
                            // Return false to let PDFView also handle the touch event
                            false
                        },
                    update = { pdfView ->
                        pdfViewRef = pdfView
                        viewWidth = pdfView.width
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
            }

            if (isLoading && errorMessage == null) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
    
    // Settings Dialog for PDF reader
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("PDF阅读设置") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("当前: 第 $currentPage / $totalPages 页")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("提示: 上下滑动翻页，双指缩放查看内容", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}
