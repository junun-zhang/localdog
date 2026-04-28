package com.example.ireader.ui.reader

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ireader.parser.EpubChapter
import com.example.ireader.parser.EpubParser
import timber.log.Timber
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpubReaderScreen(
    filePath: String?,
    title: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var chapters by remember { mutableStateOf<List<EpubChapter>>(emptyList()) }
    var currentChapterIndex by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var epubTitle by remember { mutableStateOf(title) }

    LaunchedEffect(filePath) {
        if (filePath == null) {
            errorMessage = "文件路径为空"
            isLoading = false
            return@LaunchedEffect
        }
        val file = File(filePath)
        if (!file.exists()) {
            errorMessage = "文件不存在: $filePath"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val parser = EpubParser()
            val epubInfo = parser.parse(file)
            if (epubInfo != null) {
                chapters = epubInfo.chapters
                epubTitle = epubInfo.title
            } else {
                errorMessage = "EPUB解析失败"
            }
        } catch (e: Exception) {
            errorMessage = "解析错误: ${e.message}"
        }
        isLoading = false
    }

    val currentChapter = if (chapters.isNotEmpty() && currentChapterIndex < chapters.size) {
        chapters[currentChapterIndex]
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(epubTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (currentChapterIndex > 0) {
                        IconButton(onClick = { currentChapterIndex-- }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "上一章")
                        }
                    }
                    if (currentChapterIndex < chapters.size - 1) {
                        IconButton(onClick = { currentChapterIndex++ }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "下一章")
                        }
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
                    Text("无法加载EPUB: $errorMessage", style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onNavigateBack, modifier = Modifier.padding(top = 16.dp)) {
                        Text("返回")
                    }
                }
            } else if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (currentChapter != null) {
                EpubWebView(
                    chapter = currentChapter,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (!isLoading && errorMessage == null && chapters.isNotEmpty()) {
                Text(
                    text = "${currentChapterIndex + 1} / ${chapters.size}",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EpubWebView(
    chapter: EpubChapter,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
                settings.builtInZoomControls = false
                settings.displayZoomControls = false
            }
        },
        modifier = modifier,
        update = { webView ->
            val htmlContent = wrapHtmlContent(chapter.content)
            webView.loadDataWithBaseURL(
                "file:///android_asset/",
                htmlContent,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}

private fun wrapHtmlContent(content: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: sans-serif;
                    line-height: 1.6;
                    padding: 16px;
                    margin: 0;
                    font-size: 16px;
                    color: #333;
                    background: #fff;
                }
                img {
                    max-width: 100%;
                    height: auto;
                }
                h1, h2, h3, h4, h5, h6 {
                    margin-top: 1em;
                    margin-bottom: 0.5em;
                }
                p {
                    margin-bottom: 0.5em;
                    text-indent: 2em;
                }
                a {
                    color: #1976D2;
                }
            </style>
        </head>
        <body>
            $content
        </body>
        </html>
    """.trimIndent()
}
