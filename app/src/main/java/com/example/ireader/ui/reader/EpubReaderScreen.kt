package com.example.ireader.ui.reader

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
    var allHtmlContent by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var epubTitle by remember { mutableStateOf(title) }
    var chapterCount by remember { mutableIntStateOf(0) }

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
                epubTitle = epubInfo.title
                chapterCount = epubInfo.chapters.size
                // 将所有章节拼接为一个完整的HTML，支持连续下滑阅读
                val chaptersHtml = epubInfo.chapters.joinToString(separator = "\n\n") { chapter ->
                    """
                    <div class="chapter-divider" id="chapter-${chapter.title.hashCode()}">
                        <h2 style="text-align: center; margin: 2em 0 1em 0; color: #666; border-bottom: 1px solid #eee; padding-bottom: 0.5em;">
                            ${chapter.title}
                        </h2>
                    </div>
                    ${chapter.content}
                    """.trimIndent()
                }
                allHtmlContent = chaptersHtml
            } else {
                errorMessage = "EPUB解析失败"
            }
        } catch (e: Exception) {
            errorMessage = "解析错误: ${e.message}"
            Timber.e(e, "EPUB解析失败")
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(epubTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
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
            } else if (allHtmlContent != null) {
                EpubWebView(
                    htmlContent = allHtmlContent!!,
                    modifier = Modifier.fillMaxSize()
                )
                
                // 底部章节信息
                Text(
                    text = "共 $chapterCount 章，下滑阅读全部内容",
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
    htmlContent: String,
    modifier: Modifier = Modifier
) {
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
                // 启用垂直滚动
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = false
                // 确保内容宽度适配
                settings.layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            }
        },
        modifier = modifier,
        update = { webView ->
            val fullHtml = wrapHtmlContent(htmlContent)
            webView.loadDataWithBaseURL(
                "file:///android_asset/",
                fullHtml,
                "text/html",
                "UTF-8",
                null
            )
        }
    )
}

private fun wrapHtmlContent(bodyContent: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: sans-serif;
                    line-height: 1.8;
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
                    margin-bottom: 0.8em;
                    text-indent: 2em;
                    line-height: 1.8;
                }
                a {
                    color: #1976D2;
                }
                .chapter-divider {
                    margin: 2em 0;
                    page-break-before: always;
                }
                /* 确保章节之间有足够间距 */
                h2 {
                    margin-top: 3em;
                    margin-bottom: 1em;
                }
            </style>
        </head>
        <body>
            $bodyContent
        </body>
        </html>
    """.trimIndent()
}
