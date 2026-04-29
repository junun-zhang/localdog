package com.example.ireader.ui.reader

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.webkit.JavascriptInterface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ireader.parser.EpubChapter
import com.example.ireader.parser.EpubParser
import timber.log.Timber
import java.io.File
import java.util.UUID

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
    
    // Reading progress
    // Progress is calculated from chapter position
    

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
    
    // Update chapter contents reference for WebViewClient anchor matching
    LaunchedEffect(chapters) {
        EpubWebViewClient.chapterContents = chapters.map { it.content }
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
                            Icon(Icons.Default.List, contentDescription = "目录", tint = currentTheme.textColorCompose)
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
                .pointerInput(showMenu) {
                    detectTapGestures(
                        onTap = { offset ->
                            if (offset.x > size.width * 0.3f && offset.x < size.width * 0.7f) {
                                showMenu = !showMenu
                            } else if (offset.x <= size.width * 0.3f) {
                                // Left side - scroll up / prev chapter
                                // Handled by WebView scroll
                            } else {
                                // Right side - scroll down / next chapter
                                // Handled by WebView scroll
                            }
                        }
                    )
                }
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
                
                val currentIndexForLink = chapters.indexOfFirst { it.content == currentHtml }.coerceAtLeast(0)
                val allChapterTitlesForLink = chapters.map { it.title }
                
                EpubWebView(
                    htmlContent = htmlContent ?: buildEpubHtml(chapter.content, fontSize, lineSpacing, fontFamily, pageMargin, textJustify, theme),
                    currentChapterIndex = currentIndexForLink,
                    totalChapters = chapters.size,
                    chapterTitles = allChapterTitlesForLink,
                    onChapterNavigate = { targetIndex ->
                        if (targetIndex >= 0 && targetIndex < chapters.size) {
                            currentHtml = chapters[targetIndex].content
                        }
                    },
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
    currentChapterIndex: Int = 0,
    totalChapters: Int = 0,
    chapterTitles: List<String> = emptyList(),
    onChapterNavigate: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT
                )
                webViewClient = EpubWebViewClient(
                    currentChapterIndex = currentChapterIndex,
                    totalChapters = totalChapters,
                    chapterTitles = chapterTitles,
                    onChapterNavigate = onChapterNavigate
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

/**
 * Custom WebViewClient that intercepts hyperlink clicks in EPUB content.
 *
 * EPUB table of contents typically uses links like:
 *   - <a href="#id"> (same-page anchor)
 *   - <a href="file004.html#section"> (cross-file anchor)
 *   - <a href="OEBPS/chapter3.xhtml"> (cross-file, no anchor)
 *
 * Since we load each chapter as standalone HTML via loadDataWithBaseURL,
 * these links don't resolve to real URLs. This client intercepts them and
 * maps them to chapter navigation within our reader.
 */
class EpubWebViewClient(
    private val currentChapterIndex: Int,
    private val totalChapters: Int,
    private val chapterTitles: List<String>,
    private val onChapterNavigate: (Int) -> Unit
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        return handleLinkClick(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        return handleLinkClick(view, request.url.toString())
    }

    private fun handleLinkClick(view: WebView, url: String): Boolean {
        Timber.d("EPUB link clicked: $url")

        val uri = try {
            android.net.Uri.parse(url)
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse URL: $url")
            return true // consume invalid URLs
        }

        val scheme = uri.scheme
        val path = uri.path ?: ""
        val fragment = uri.fragment

        // Handle external URLs (http/https) - open in browser
        if (scheme == "http" || scheme == "https") {
            try {
                val context = view.context
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse(url)
                )
                context.startActivity(intent)
            } catch (e: Exception) {
                Timber.e(e, "Failed to open URL: $url")
            }
            return true // consume the link
        }

        // Handle same-page anchor (#something)
        if (path.isEmpty() && fragment != null) {
            // Let WebView handle internal anchor scrolling
            return false
        }

        // Handle cross-file links - try to match to a chapter
        // The path might be: file004.html, OEBPS/chapter3.xhtml, Text/part004.html, etc.
        val matchedIndex = tryMatchChapter(path, fragment)
        if (matchedIndex >= 0) {
            onChapterNavigate(matchedIndex)
            return true // consume the link, we handle navigation
        }

        // If we can't match, let WebView try (will likely fail silently)
        return false
    }

    /**
     * Try to match a link path to a chapter index.
     *
     * Strategy:
     * 1. Extract filename from path (e.g., "file004.html" from "OEBPS/file004.html")
     * 2. Try to match filename against all chapter contents' source filenames
     * 3. Fall back to matching against chapter titles
     * 4. Return -1 if no match found
     */
    private fun tryMatchChapter(path: String, fragment: String?): Int {
        val filename = path.substringAfterLast("/").substringBefore("#")

        if (filename.isBlank()) return -1

        // Strategy 1: Match filename against chapter content
        // Since EpubParser stores the raw HTML content, we can look for
        // the filename in the content's source reference or try to match
        // the fragment anchor across all chapters

        if (fragment != null) {
            // Search for the anchor ID in all chapters
            val anchorId = fragment
            for (i in chapterContents.indices) {
                if (i == currentChapterIndex) continue
                val content = chapterContents[i]
                if (content.contains("id=\"$anchorId\"") ||
                    content.contains("id='$anchorId'") ||
                    content.contains("name=\"$anchorId\"") ||
                    content.contains("name='$anchorId'")) {
                    return i
                }
            }
        }

        // Strategy 2: Try to match by title
        // Some EPUBs link to chapter titles directly
        val titleFromPath = filename
            .removeSuffix(".html")
            .removeSuffix(".xhtml")
            .removeSuffix(".htm")

        for (i in chapterTitles.indices) {
            if (i == currentChapterIndex) continue
            if (chapterTitles[i].contains(titleFromPath, ignoreCase = true) ||
                titleFromPath.contains(chapterTitles[i], ignoreCase = true)) {
                return i
            }
        }

        // Strategy 3: If path contains a recognizable chapter number pattern
        val numberPattern = Regex("(\\d+)")
        val numberMatch = numberPattern.find(filename)
        if (numberMatch != null) {
            val pageNum = numberMatch.groupValues[1].toIntOrNull()
            if (pageNum != null && pageNum > 0 && pageNum <= totalChapters) {
                return pageNum - 1 // 1-based to 0-based
            }
        }

        return -1
    }

    companion object {
        // Store chapter contents for anchor matching
        var chapterContents: List<String> = emptyList()
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
            <script>
                // Intercept all link clicks to ensure they go through shouldOverrideUrlLoading
                document.addEventListener('click', function(e) {
                    var link = e.target.closest('a');
                    if (link && link.href) {
                        e.preventDefault();
                        // Navigate to the link - this triggers shouldOverrideUrlLoading
                        window.location = link.href;
                    }
                }, true);
            </script>
        </head>
        <body>
            $content
        </body>
        </html>
    """.trimIndent()
}
