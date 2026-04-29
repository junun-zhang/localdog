package com.example.ireader.ui.reader

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ireader.parser.EpubChapter
import com.example.ireader.parser.EpubParser
import timber.log.Timber
import java.io.File

// Reader theme definitions (same as TXT reader)
data class EpubReaderTheme(
    val name: String,
    val label: String,
    val backgroundColor: String, // CSS color for WebView
    val textColor: String,
    val barColor: Color, // Compose color for UI bars
    val textColorCompose: Color // Compose color for UI text
)

val epubReaderThemes = listOf(
    EpubReaderTheme("light", "日间", "#FFFFFF", "#333333", Color.White, Color(0xFF333333)),
    EpubReaderTheme("sepia", "护眼", "#F5F0E8", "#5C4B37", Color(0xFFF5F0E8), Color(0xFF5C4B37)),
    EpubReaderTheme("dark", "夜间", "#1A1A2E", "#CCCCCC", Color(0xFF1A1A2E), Color(0xFFCCCCCC)),
    EpubReaderTheme("green", "绿色", "#E8F5E9", "#33691E", Color(0xFFE8F5E9), Color(0xFF33691E))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpubReaderScreen(
    filePath: String?,
    @Suppress("UNUSED_PARAMETER") bookId: String,
    title: String,
    onNavigateBack: () -> Unit
) {
    var chapters by remember { mutableStateOf<List<EpubChapter>>(emptyList()) }
    var currentHtml by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var epubTitle by remember { mutableStateOf(title) }
    
    // UI state - tap to show/hide
    var showMenu by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showTocDialog by remember { mutableStateOf(false) }
    
    // Settings state
    var fontSize by remember { mutableFloatStateOf(16f) }
    var lineSpacing by remember { mutableFloatStateOf(1.8f) }
    var theme by remember { mutableStateOf("light") }
    var fontFamily by remember { mutableStateOf("sans-serif") } // sans-serif or serif
    var pageMargin by remember { mutableIntStateOf(16) }
    var dialogFontSize by remember { mutableFloatStateOf(16f) }
    var dialogLineSpacing by remember { mutableFloatStateOf(1.8f) }
    var dialogTheme by remember { mutableStateOf("light") }
    var dialogFontFamily by remember { mutableStateOf("sans-serif") }
    var dialogPageMargin by remember { mutableIntStateOf(16) }
    var textJustify by remember { mutableStateOf(true) }
    var dialogTextJustify by remember { mutableStateOf(true) }

    // Load EPUB file
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
                chapters = epubInfo.chapters
            } else {
                errorMessage = "EPUB解析失败"
            }
        } catch (e: Exception) {
            errorMessage = "解析错误: ${e.message}"
            Timber.e(e, "EPUB解析失败")
        }
        isLoading = false
    }
    
    val currentTheme = epubReaderThemes.find { it.name == theme } ?: epubReaderThemes[0]

    // Build HTML with current settings
    val htmlContent = remember(currentHtml, fontSize, lineSpacing, theme, fontFamily, pageMargin, textJustify) {
        currentHtml?.let { buildEpubHtml(it, fontSize, lineSpacing, fontFamily, pageMargin, textJustify, theme) }
    }

    Scaffold(
        containerColor = currentTheme.barColor,
        topBar = {
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TopAppBar(
                    title = { Text(epubTitle, color = currentTheme.textColorCompose) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回", tint = currentTheme.textColorCompose)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showTocDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "目录", tint = currentTheme.textColorCompose)
                        }
                        IconButton(onClick = {
                            dialogFontSize = fontSize
                            dialogLineSpacing = lineSpacing
                            dialogTheme = theme
                            dialogFontFamily = fontFamily
                            dialogPageMargin = pageMargin
                            dialogTextJustify = textJustify
                            showSettingsDialog = true
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置", tint = currentTheme.textColorCompose)
                        }
                    }
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.background(currentTheme.barColor)) {
                    // Progress bar
                    val currentChapterIndex = chapters.indexOfFirst { it.content == currentHtml }.coerceAtLeast(0)
                    val progress = if (chapters.isNotEmpty()) (currentChapterIndex + 1).toFloat() / chapters.size else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                        color = currentTheme.textColorCompose,
                        trackColor = currentTheme.barColor
                    )
                    // Chapter info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${chapters.indexOfFirst { it.content == currentHtml } + 1} / ${chapters.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = currentTheme.textColorCompose
                        )
                        Text(
                            text = "进度: ${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = currentTheme.textColorCompose
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
                .background(currentTheme.barColor)
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
            } else if (chapters.isNotEmpty()) {
                // Show current chapter in WebView
                val chapter = chapters.find { it.content == currentHtml } ?: chapters[0]
                LaunchedEffect(Unit) {
                    if (currentHtml == null) {
                        currentHtml = chapters[0].content
                    }
                }
                
                EpubWebView(
                    htmlContent = htmlContent ?: buildEpubHtml(chapter.content, fontSize, lineSpacing, fontFamily, pageMargin, textJustify, theme),
                    onScreenTap = { showMenu = !showMenu },
                    modifier = Modifier.fillMaxSize()
                )
                
                // Chapter navigation buttons (only show when menu is visible)
                AnimatedVisibility(
                    visible = showMenu,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(currentTheme.barColor.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val currentIndex = chapters.indexOfFirst { it.content == currentHtml }.coerceAtLeast(0)
                        if (currentIndex > 0) {
                            Button(
                                onClick = {
                                    currentHtml = chapters[currentIndex - 1].content
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.textColorCompose)
                            ) {
                                Text("上一章", color = currentTheme.barColor)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(80.dp))
                        }
                        
                        Text(
                            text = chapter.title,
                            style = MaterialTheme.typography.bodySmall,
                            color = currentTheme.textColorCompose,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        
                        if (currentIndex < chapters.size - 1) {
                            Button(
                                onClick = {
                                    currentHtml = chapters[currentIndex + 1].content
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = currentTheme.textColorCompose)
                            ) {
                                Text("下一章", color = currentTheme.barColor)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(80.dp))
                        }
                    }
                }
            }
        }
    }
    
    // Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("阅读设置") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("字体大小: ${dialogFontSize.toInt()}sp", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = dialogFontSize,
                        onValueChange = { dialogFontSize = it },
                        valueRange = 12f..28f,
                        steps = 15
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("行距: ${"%.1f".format(dialogLineSpacing)}x", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = dialogLineSpacing,
                        onValueChange = { dialogLineSpacing = it },
                        valueRange = 1.0f..2.5f,
                        steps = 14
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("页边距: ${dialogPageMargin}px", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = dialogPageMargin.toFloat(),
                        onValueChange = { dialogPageMargin = it.toInt() },
                        valueRange = 0f..40f,
                        steps = 10
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("字体", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("sans-serif" to "无衬线", "serif" to "衬线").forEach { (font, label) ->
                            Card(
                                modifier = Modifier
                                    .clickable { dialogFontFamily = font }
                                    .border(
                                        width = if (dialogFontFamily == font) 2.dp else 0.dp,
                                        color = if (dialogFontFamily == font) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(label, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("段落对齐", style = MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(true to "两端对齐", false to "左对齐").forEach { (justify, label) ->
                            Card(
                                modifier = Modifier
                                    .clickable { dialogTextJustify = justify }
                                    .border(
                                        width = if (dialogTextJustify == justify) 2.dp else 0.dp,
                                        color = if (dialogTextJustify == justify) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(label, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("主题", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        epubReaderThemes.forEach { rTheme ->
                            Card(
                                modifier = Modifier
                                    .clickable { dialogTheme = rTheme.name }
                                    .border(
                                        width = if (dialogTheme == rTheme.name) 2.dp else 0.dp,
                                        color = if (dialogTheme == rTheme.name) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(rTheme.label, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    fontSize = dialogFontSize
                    lineSpacing = dialogLineSpacing
                    theme = dialogTheme
                    fontFamily = dialogFontFamily
                    pageMargin = dialogPageMargin
                    textJustify = dialogTextJustify
                    showSettingsDialog = false
                }) {
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
    
    // TOC Dialog
    if (showTocDialog) {
        AlertDialog(
            onDismissRequest = { showTocDialog = false },
            title = { Text("目录") },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                ) {
                    itemsIndexed(chapters) { index, chapter ->
                        val currentIndex = chapters.indexOfFirst { it.content == currentHtml }.coerceAtLeast(0)
                        val isSelected = index == currentIndex
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    currentHtml = chapter.content
                                    showTocDialog = false
                                }
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    else Color.Transparent
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${index + 1}. ${chapter.title}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "当前章节",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTocDialog = false }) {
                    Text("关闭")
                }
            }
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EpubWebView(
    htmlContent: String,
    onScreenTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Stable JS interface - survives recomposition
    val tapHandler = remember { TapJsHandler() }
    // Update the callback on each recomposition
    tapHandler.onTap = onScreenTap

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(false)
                settings.builtInZoomControls = false
                settings.displayZoomControls = false
                isVerticalScrollBarEnabled = true
                isHorizontalScrollBarEnabled = false
                settings.layoutAlgorithm = android.webkit.WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
                
                // Register JS interface for tap callback
                addJavascriptInterface(tapHandler, "TapBridge")
                
                webViewClient = WebViewClient()
            }
        },
        modifier = modifier,
        update = { webView ->
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

/** JS interface class for tap callback - must be top-level and non-inner class */
class TapJsHandler {
    @Volatile
    var onTap: (() -> Unit)? = null

    @JavascriptInterface
    fun onTap() {
        onTap?.invoke()
    }
}

private fun buildEpubHtml(
    content: String,
    fontSize: Float,
    lineSpacing: Float,
    fontFamily: String,
    pageMargin: Int,
    justify: Boolean,
    themeName: String
): String {
    val theme = epubReaderThemes.find { it.name == themeName } ?: epubReaderThemes[0]
    val textAlign = if (justify) "justify" else "left"
    
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
            <style>
                html, body {
                    margin: 0;
                    padding: 0;
                    width: 100%;
                    height: 100%;
                }
                body {
                    font-family: $fontFamily;
                    font-size: ${fontSize}px;
                    line-height: $lineSpacing;
                    padding: ${pageMargin}px;
                    margin: 0;
                    color: ${theme.textColor};
                    background: ${theme.backgroundColor};
                    text-align: $textAlign;
                    -webkit-hyphens: auto;
                    word-wrap: break-word;
                }
                img {
                    max-width: 100%;
                    height: auto;
                    display: block;
                    margin: 1em auto;
                }
                h1, h2, h3, h4, h5, h6 {
                    margin-top: 1em;
                    margin-bottom: 0.5em;
                    line-height: 1.3;
                }
                p {
                    margin-bottom: 0.5em;
                    margin-top: 0;
                    text-indent: 2em;
                }
                a {
                    color: #1976D2;
                    text-decoration: none;
                }
                /* Reset EPUB default styles */
                div, span {
                    font-family: inherit;
                    font-size: inherit;
                    line-height: inherit;
                    color: inherit;
                }
            </style>
        </head>
        <body>
            $content
            <script>
                // Detect taps (click events only fire for actual taps, not scrolls/swipes)
                document.body.addEventListener('click', function(e) {
                    var rect = document.body.getBoundingClientRect();
                    var x = e.clientX - rect.left;
                    // Only trigger menu toggle for center 30%-70% area
                    if (x > rect.width * 0.3 && x < rect.width * 0.7) {
                        if (typeof TapBridge !== 'undefined') {
                            TapBridge.onTap();
                        }
                    }
                });
            </script>
        </body>
        </html>
    """.trimIndent()
}
