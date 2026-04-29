package com.example.ireader.ui.reader

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.view.ViewGroup.LayoutParams
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ireader.parser.EpubBookInfo
import com.example.ireader.parser.EpubChapter
import timber.log.Timber
import java.io.File

// ── Theme data ────────────────────────────────────────────────────────
data class EpubReaderTheme(
    val name: String,
    val label: String,
    val backgroundColor: String,
    val textColor: String,
    val barColor: Color,
    val textColorCompose: Color
)

val epubReaderThemes = listOf(
    EpubReaderTheme("light", "日间", "#ffffff", "#333333", Color.White, Color(0xFF333333)),
    EpubReaderTheme("sepia", "护眼", "#f5f0e8", "#5c4b37", Color(0xFFF5F0E8), Color(0xFF5C4B37)),
    EpubReaderTheme("dark", "夜间", "#1a1a2e", "#cccccc", Color(0xFF1A1A2E), Color(0xFFCCCCCC)),
    EpubReaderTheme("green", "绿色", "#e8f5e9", "#33691e", Color(0xFFE8F5E9), Color(0xFF33691E))
)

/**
 * JavaScript bridge: receives page-dimension info + tap events from WebView.
 */
class EpubJsBridge {
    @Volatile var onContentReady: ((totalScrollHeight: Int, viewportHeight: Int) -> Unit)? = null
    @Volatile var onScrollChanged: ((scrollY: Int, maxScroll: Int) -> Unit)? = null
    @Volatile var onCenterTap: (() -> Unit)? = null
    @Volatile var onPrevPage: (() -> Unit)? = null
    @Volatile var onNextPage: (() -> Unit)? = null

    /** Called once after WebView finishes rendering to report content dimensions. */
    @JavascriptInterface
    fun contentReady(scrollHeight: Int, viewportHeight: Int) {
        onContentReady?.invoke(scrollHeight, viewportHeight)
    }

    /** Called on scroll to report current position. */
    @JavascriptInterface
    fun scrollChanged(scrollY: Int, maxScroll: Int) {
        onScrollChanged?.invoke(scrollY, maxScroll)
    }

    @JavascriptInterface
    fun centerTap() {
        onCenterTap?.invoke()
    }

    @JavascriptInterface
    fun prevPage() {
        onPrevPage?.invoke()
    }

    @JavascriptInterface
    fun nextPage() {
        onNextPage?.invoke()
    }
}

/**
 * Build merged HTML from all chapters for continuous scrolling.
 * Each chapter gets an ID anchor for potential TOC jumping.
 */
private fun buildMergedHtml(
    chapters: List<EpubChapter>,
    extractDirPath: String?,
    coverImagePath: String?,
    bookTitle: String,
    bookAuthors: List<String>?,
    fontSize: Float,
    lineSpacing: Float,
    theme: String,
    fontFamily: String,
    textJustify: Boolean
): String {
    val textAlign = if (textJustify) "justify" else "left"
    val themeObj = epubReaderThemes.find { it.name == theme } ?: epubReaderThemes[0]

    val sb = StringBuilder()
    sb.appendLine("<!DOCTYPE html>")
    sb.appendLine("<html>")
    sb.appendLine("<head>")
    sb.appendLine("""<meta charset="UTF-8">""")
    sb.appendLine("""<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">""")
    sb.appendLine("""
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    html, body {
        width: 100%;
        min-height: 100%;
        font-family: $fontFamily;
        font-size: ${fontSize}px;
        line-height: $lineSpacing;
        color: ${themeObj.textColor};
        background: ${themeObj.backgroundColor};
        text-align: $textAlign;
        -webkit-hyphens: auto;
        word-wrap: break-word;
        -webkit-text-size-adjust: 100%;
        scroll-behavior: smooth;
    }
    body { padding: 0; }
    .content-padding { padding: 20px 16px; }
    .cover-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 100vh;
        padding: 32px 16px;
        text-align: center;
    }
    .cover-image {
        max-width: 70%;
        height: auto;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        margin-bottom: 24px;
    }
    .book-title {
        font-size: 1.5em;
        font-weight: bold;
        margin-bottom: 8px;
        line-height: 1.3;
    }
    .book-authors {
        font-size: 1em;
        opacity: 0.7;
        margin-bottom: 12px;
    }
    .start-hint {
        display: inline-block;
        padding: 12px 32px;
        border-radius: 24px;
        background: ${themeObj.textColor}22;
        color: ${themeObj.textColor};
        margin-top: 16px;
        font-size: 1.1em;
    }
    .chapter-title {
        text-align: center;
        text-indent: 0;
        margin: 1em 0;
        font-size: 1.2em;
        font-weight: bold;
    }
    .chapter-divider {
        height: 1px;
        background: linear-gradient(to right, transparent, #aaa, transparent);
        margin: 0;
        border: none;
    }
</style>
<script>
(function() {
    var bridge = window.AndroidBridge;
    var viewportH = 0;
    var scrollH = 0;
    var pageFlipping = false;

    function reportDimensions() {
        viewportH = window.innerHeight;
        scrollH = document.body.scrollHeight;
        if (bridge) {
            bridge.contentReady(scrollH, viewportH);
        }
        // Report initial scroll
        reportScroll();
    }

    function reportScroll() {
        if (bridge) {
            bridge.scrollChanged(window.scrollY, Math.max(0, scrollH - viewportH));
        }
    }

    // Report on resize / orientation change
    window.addEventListener('resize', function() {
        setTimeout(reportDimensions, 300);
    });

    // Report on scroll (throttled)
    var scrollTimer = null;
    window.addEventListener('scroll', function() {
        if (scrollTimer) clearTimeout(scrollTimer);
        scrollTimer = setTimeout(reportScroll, 100);
    });

    // Tap handling
    document.body.addEventListener('click', function(e) {
        // Ignore link clicks
        var t = e.target;
        while (t && t !== document.body) {
            if (t.tagName === 'A' || t.tagName === 'IMG') return;
            t = t.parentElement;
        }

        var rect = document.body.getBoundingClientRect();
        var x = e.clientX - rect.left;
        var w = rect.width;

        if (x > w * 0.3 && x < w * 0.7) {
            // Center tap → toggle menu
            if (bridge) bridge.centerTap();
        } else if (x >= w * 0.7) {
            // Right tap → next page
            if (bridge) bridge.nextPage();
        } else {
            // Left tap → prev page
            if (bridge) bridge.prevPage();
        }
    });

    // Report when DOM is ready
    if (document.readyState === 'complete') {
        reportDimensions();
    } else {
        window.addEventListener('load', reportDimensions);
    }
})();
</script>
    """.trimIndent())
    sb.appendLine("</head>")
    sb.appendLine("<body>")

    // ═══ Cover page as first "page" of the book (掌阅-style) ═══
    sb.appendLine("<div class=\"cover-container\" id=\"_cover_page\">")
    if (coverImagePath != null) {
        sb.appendLine("""<img class="cover-image" src="file://${File(coverImagePath).canonicalFile.absolutePath}" alt="封面">""")
    } else {
        sb.appendLine("""<div style="width:70%;aspect-ratio:0.7;background:#e0e0e0;border-radius:8px;display:flex;align-items:center;justify-content:center;margin-bottom:24px;"><span style="color:#999;">暂无封面</span></div>""")
    }
    sb.appendLine("<div class=\"book-title\">${escapeHtml(bookTitle)}</div>")
    if (bookAuthors?.isNotEmpty() == true) {
        sb.appendLine("<div class=\"book-authors\">${escapeHtml(bookAuthors.joinToString(" / "))}</div>")
    }
    sb.appendLine("<div class=\"start-hint\">点击开始阅读</div>")
    sb.appendLine("</div>")

    // ═══ Chapter content ═══
    chapters.forEachIndexed { index, chapter ->
        // Anchor for TOC jumping
        sb.appendLine("""<a id="chapter_$index"></a>""")
        // Chapter content wrapper with padding
        sb.appendLine("<div class=\"content-padding\">")
        // Chapter title
        sb.appendLine("<div class=\"chapter-title\">${escapeHtml(chapter.title)}</div>")
        sb.appendLine("<hr class=\"chapter-divider\">")

        var content = chapter.content
        // Strip outer HTML wrappers
        content = content.replace(Regex("""(?s)<html[^>]*>"""), "")
        content = content.replace(Regex("""(?s)</html>"""), "")
        content = content.replace(Regex("""(?s)<head>.*?</head>"""), "")
        content = content.replace(Regex("""(?s)<body[^>]*>"""), "")
        content = content.replace(Regex("""(?s)</body>"""), "")
        content = content.replace(Regex("""(?i)<!DOCTYPE[^>]*>"""), "")

        // Fix relative image paths
        if (extractDirPath != null) {
            content = content.replace(Regex("""src="([^"]+)"""")) { match ->
                val imgPath = match.groupValues[1]
                if (imgPath.startsWith("http") || imgPath.startsWith("file:") || imgPath.startsWith("data:"))
                    match.value
                else
                    """src="file://${File(extractDirPath, imgPath).canonicalFile.absolutePath}""""
            }
        }

        sb.appendLine(content)
        // Divider between chapters
        sb.appendLine("</div>")  // .content-padding
        if (index < chapters.size - 1) {
            sb.appendLine("<hr class=\"chapter-divider\">")
        }
    }

    sb.appendLine("</body>")
    sb.appendLine("</html>")

    return sb.toString()
}

private fun escapeHtml(text: String): String {
    return text
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
}

@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpubReaderScreen(
    filePath: String?,
    bookId: String?,
    title: String,
    epubInfo: EpubBookInfo?,
    onNavigateBack: () -> Unit
) {
    // ── Data ──────────────────────────────────────────────────────────
    val allChapters = remember { epubInfo?.chapters ?: emptyList() }
    val chapterAnchors = remember { epubInfo?.chapterAnchors ?: emptyList<Pair<String, String>>() }
    val extractDirPath = remember { epubInfo?.extractDirPath }
    val coverImagePath = remember { epubInfo?.coverImagePath }

    // ── Reading state ─────────────────────────────────────────────────
    var coverPageShown by remember { mutableStateOf(true) }  // Show cover first, then WebView
    var showMenu by remember { mutableStateOf(false) }
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showTocDialog by remember { mutableStateOf(false) }

    // Page-based navigation state
    var currentPage by remember { mutableIntStateOf(1) }
    var totalPages by remember { mutableIntStateOf(1) }
    var currentScrollY by remember { mutableIntStateOf(0) }
    var viewportHeight by remember { mutableIntStateOf(0) }
    var totalScrollHeight by remember { mutableIntStateOf(0) }

    // ── Reading settings (persisted) ──────────────────────────────────
    var fontSize by rememberSaveable { mutableFloatStateOf(18f) }
    var lineSpacing by rememberSaveable { mutableFloatStateOf(1.8f) }
    var theme by rememberSaveable { mutableStateOf("light") }
    var fontFamily by rememberSaveable { mutableStateOf("sans-serif") }
    var textJustify by rememberSaveable { mutableStateOf(true) }
    // Brightness: 0.0 = full brightness (no dimming), 1.0 = max dimming
    var brightnessDim by rememberSaveable { mutableFloatStateOf(0f) }

    // Dialog temp state
    var dialogFontSize by remember { mutableFloatStateOf(18f) }
    var dialogLineSpacing by remember { mutableFloatStateOf(1.8f) }
    var dialogTheme by remember { mutableStateOf("light") }
    var dialogFontFamily by remember { mutableStateOf("sans-serif") }
    var dialogTextJustify by remember { mutableStateOf(true) }
    var dialogBrightnessDim by remember { mutableFloatStateOf(0f) }

    val currentTheme = epubReaderThemes.find { it.name == theme } ?: epubReaderThemes[0]

    // WebView ref for JS re-injection on settings change
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // JS bridge
    val bridge = remember { EpubJsBridge() }

    // ── Page navigation helpers ───────────────────────────────────────
    fun goToPage(pageNum: Int) {
        if (pageNum < 1 || pageNum > totalPages || totalPages <= 0) return
        currentPage = pageNum
        val targetScrollY = ((pageNum - 1).toFloat() / totalPages.toFloat() * totalScrollHeight).toInt()
        webViewRef?.evaluateJavascript(
            "window.scrollTo({top: $targetScrollY, behavior: 'smooth'});", null
        )
        showMenu = false
    }

    fun nextPage() {
        if (currentPage < totalPages) {
            goToPage(currentPage + 1)
        }
    }

    fun prevPage() {
        if (currentPage > 1) {
            goToPage(currentPage - 1)
        }
    }

    // Wire bridge callbacks
    LaunchedEffect(Unit) {
        bridge.onContentReady = { scrollH, vpH ->
            totalScrollHeight = scrollH
            viewportHeight = vpH
            val pages = if (vpH > 0) kotlin.math.ceil(scrollH.toDouble() / vpH.toDouble()).toInt() else 1
            totalPages = pages.coerceAtLeast(1)
        }
        bridge.onScrollChanged = { scrollY, _ ->
            currentScrollY = scrollY
            if (viewportHeight > 0 && totalScrollHeight > 0) {
                val newPage = (scrollY.toDouble() / viewportHeight.toDouble()).toInt() + 1
                currentPage = newPage.coerceIn(1, totalPages)
            }
        }
        bridge.onCenterTap = { showMenu = !showMenu }
        bridge.onPrevPage = { prevPage() }
        bridge.onNextPage = { nextPage() }
    }

    // ── Cover image bitmap ────────────────────────────────────────────
    val coverBitmap = remember(coverImagePath) {
        coverImagePath?.let { path ->
            try {
                BitmapFactory.decodeFile(path)
            } catch (e: Exception) {
                Timber.e(e, "Failed to decode cover: $path")
                null
            }
        }
    }

    // ── Build merged HTML from current settings ───────────────────────
    val mergedHtml = remember(allChapters, extractDirPath, coverImagePath, title, fontSize, lineSpacing, theme, fontFamily, textJustify) {
        if (allChapters.isEmpty()) ""
        else buildMergedHtml(allChapters, extractDirPath, coverImagePath, title, epubInfo?.authors,
            fontSize, lineSpacing, theme, fontFamily, textJustify)
    }

    // ── UI ────────────────────────────────────────────────────────────
    Scaffold(
        containerColor = currentTheme.barColor,
        topBar = {
            AnimatedVisibility(visible = showMenu, enter = fadeIn(), exit = fadeOut()) {
                TopAppBar(
                    title = {
                        Column {
                            Text(title, color = currentTheme.textColorCompose, maxLines = 1,
                                style = MaterialTheme.typography.titleMedium)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回",
                                tint = currentTheme.textColorCompose)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showTocDialog = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "目录",
                                tint = currentTheme.textColorCompose)
                        }
                        IconButton(onClick = {
                            dialogFontSize = fontSize
                            dialogLineSpacing = lineSpacing
                            dialogTheme = theme
                            dialogFontFamily = fontFamily
                            dialogTextJustify = textJustify
                            dialogBrightnessDim = brightnessDim
                            showSettingsSheet = true
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置",
                                tint = currentTheme.textColorCompose)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = currentTheme.barColor)
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(visible = showMenu, enter = fadeIn(), exit = fadeOut()) {
                BottomAppBar(
                    containerColor = currentTheme.barColor,
                    tonalElevation = 0.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Previous page button
                        TextButton(
                            onClick = { prevPage() },
                            enabled = currentPage > 1,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text("上一页", style = MaterialTheme.typography.bodySmall,
                                color = if (currentPage > 1) currentTheme.textColorCompose
                                    else currentTheme.textColorCompose.copy(alpha = 0.3f))
                        }

                        // Page progress - 掌阅 style
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "第${currentPage}页 / 共${totalPages}页",
                                style = MaterialTheme.typography.bodyMedium,
                                color = currentTheme.textColorCompose
                            )
                            Text(
                                text = if (totalPages > 0) {
                                    val pct = (currentPage.toFloat() / totalPages.toFloat() * 100).toInt()
                                    "阅读进度 ${pct}%"
                                } else "",
                                style = MaterialTheme.typography.bodySmall,
                                color = currentTheme.textColorCompose.copy(alpha = 0.7f),
                                maxLines = 1
                            )
                        }

                        // Next page button
                        TextButton(
                            onClick = { nextPage() },
                            enabled = currentPage < totalPages,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text("下一页", style = MaterialTheme.typography.bodySmall,
                                color = if (currentPage < totalPages) currentTheme.textColorCompose
                                    else currentTheme.textColorCompose.copy(alpha = 0.3f))
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(android.graphics.Color.parseColor(currentTheme.backgroundColor)))
        ) {
            if (coverPageShown) {
                // ═══ Cover page overlay (Compose, not WebView) ═══
                CoverPageOverlay(
                    coverBitmap = coverBitmap,
                    title = title,
                    authors = epubInfo?.authors,
                    chapterCount = allChapters.size,
                    currentTheme = currentTheme,
                    onStartReading = {
                        coverPageShown = false
                        showMenu = true
                        // Load merged HTML into WebView after composition
                    }
                )
            } else {
                // ═══ Continuous scroll WebView ═══
                if (mergedHtml.isNotBlank()) {
                    AndroidView(
                        factory = { ctx ->
                            WebView(ctx).apply {
                                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

                                webViewClient = object : WebViewClient() {
                                    override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                                        val url = request?.url?.toString() ?: return false
                                        if (url.startsWith("http://") || url.startsWith("https://")) {
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse(url)
                                                )
                                                ctx.startActivity(intent)
                                            } catch (e: Exception) {
                                                Timber.e(e, "Failed to open URL")
                                            }
                                            return true
                                        }
                                        // Internal anchor links → scroll to chapter
                                        if (url.startsWith("#chapter_")) {
                                            val index = url.removePrefix("#chapter_").toIntOrNull()
                                            if (index != null && index in allChapters.indices) {
                                                webViewRef?.evaluateJavascript(
                                                    """document.getElementById('chapter_$index')?.scrollIntoView({behavior:'smooth'});""",
                                                    null
                                                )
                                            }
                                            return true
                                        }
                                        return false
                                    }

                                    @Deprecated("Deprecated in Java")
                                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                                        url ?: return false
                                        if (url.startsWith("http://") || url.startsWith("https://")) {
                                            try {
                                                val intent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse(url)
                                                )
                                                ctx.startActivity(intent)
                                            } catch (e: Exception) {
                                                Timber.e(e, "Failed to open URL")
                                            }
                                            return true
                                        }
                                        if (url.startsWith("#chapter_")) {
                                            val index = url.removePrefix("#chapter_").toIntOrNull()
                                            if (index != null && index in allChapters.indices) {
                                                webViewRef?.evaluateJavascript(
                                                    """document.getElementById('chapter_$index')?.scrollIntoView({behavior:'smooth'});""",
                                                    null
                                                )
                                            }
                                            return true
                                        }
                                        return false
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        // Inject tap bridge handler for link clicks
                                        view?.evaluateJavascript("""
                                            (function() {
                                                var links = document.querySelectorAll('a[href^="#chapter_"]');
                                                links.forEach(function(link) {
                                                    link.addEventListener('click', function(e) {
                                                        e.preventDefault();
                                                        var href = this.getAttribute('href');
                                                        var el = document.getElementById(href.substring(1));
                                                        if (el) el.scrollIntoView({behavior:'smooth'});
                                                    });
                                                });
                                            })();
                                        """.trimIndent(), null)
                                    }
                                }

                                settings.javaScriptEnabled = true
                                settings.domStorageEnabled = true
                                settings.useWideViewPort = true
                                settings.loadWithOverviewMode = true
                                settings.builtInZoomControls = false
                                settings.setSupportZoom(false)
                                settings.allowFileAccess = true
                                isVerticalScrollBarEnabled = false  // Hide scrollbar for clean look
                                isHorizontalScrollBarEnabled = false
                                overScrollMode = android.view.View.OVER_SCROLL_NEVER

                                addJavascriptInterface(bridge, "AndroidBridge")

                                // Load merged HTML content
                                val baseUrl = extractDirPath?.let { "file://${File(it).absolutePath}/" } ?: ""
                                loadDataWithBaseURL(baseUrl, mergedHtml, "text/html", "UTF-8", null)

                                webViewRef = this
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { wv ->
                            webViewRef = wv
                        }
                    )

                    // ═══ Brightness dimming overlay ═══
                    // Semi-transparent overlay when brightness is dimmed
                    if (brightnessDim > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = brightnessDim * 0.45f))
                                .pointerInput(Unit) {
                                    // Pass through touch events to WebView
                                    // This block intentionally empty — events fall through
                                }
                        )
                    }
                } else {
                    // Fallback — no content
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("无法加载EPUB文件", style = MaterialTheme.typography.bodyMedium,
                            color = currentTheme.textColorCompose)
                    }
                }

            // ═══ Page indicator (always visible when reading) ═══
            if (totalPages > 1 && !showMenu) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.Black.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "$currentPage / $totalPages",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            // ═══ Reading progress bar (always visible thin line at top) ═══
            if (totalPages > 0) {
                LinearProgressIndicator(
                    progress = { currentPage.toFloat() / totalPages.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .height(2.dp),
                    color = currentTheme.textColorCompose.copy(alpha = 0.6f),
                    trackColor = Color.Transparent,
                )
            }
        }  // close coverPageShown else block
        }
    }

    // ═══ Bottom Sheet Settings Panel (掌阅-style) ═══
    if (showSettingsSheet) {
        EpubSettingsBottomSheet(
            fontSize = dialogFontSize,
            lineSpacing = dialogLineSpacing,
            theme = dialogTheme,
            fontFamily = dialogFontFamily,
            textJustify = dialogTextJustify,
            brightnessDim = dialogBrightnessDim,
            onFontSizeChange = { dialogFontSize = it },
            onLineSpacingChange = { dialogLineSpacing = it },
            onThemeChange = { dialogTheme = it },
            onFontFamilyChange = { dialogFontFamily = it },
            onTextJustifyChange = { dialogTextJustify = it },
            onBrightnessDimChange = { dialogBrightnessDim = it },
            onApply = {
                fontSize = dialogFontSize
                lineSpacing = dialogLineSpacing
                theme = dialogTheme
                fontFamily = dialogFontFamily
                textJustify = dialogTextJustify
                brightnessDim = dialogBrightnessDim
                showSettingsSheet = false
                // Reload WebView with new settings
                webViewRef?.let { wv ->
                    val newHtml = buildMergedHtml(allChapters, extractDirPath,
                        coverImagePath, title, epubInfo?.authors,
                        dialogFontSize, dialogLineSpacing, dialogTheme, dialogFontFamily, dialogTextJustify)
                    val baseUrl = extractDirPath?.let { "file://${File(it).absolutePath}/" } ?: ""
                    wv.loadDataWithBaseURL(baseUrl, newHtml, "text/html", "UTF-8", null)
                    // Recalculate
                    totalScrollHeight = 0
                    viewportHeight = 0
                    currentPage = 1
                }
            },
            onDismiss = { showSettingsSheet = false }
        )
    }

    // ═══ TOC Dialog ═══
    if (showTocDialog) {
        AlertDialog(
            onDismissRequest = { showTocDialog = false },
            title = { Text("目录") },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                    state = rememberLazyListState().also { state ->
                        LaunchedEffect(currentPage) {
                            // Find current chapter index based on current page
                            // Approximate: page / totalPages * chapters.size
                            val approxIndex = ((currentPage.toFloat() / totalPages.toFloat()) * allChapters.size).toInt()
                                .coerceIn(0, allChapters.size - 1)
                            if (approxIndex in chapterAnchors.indices) {
                                state.scrollToItem(approxIndex)
                            }
                        }
                    }
                ) {
                    itemsIndexed(chapterAnchors) { index, (chapterTitleItem, _) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Scroll to chapter in merged content
                                    showTocDialog = false
                                    webViewRef?.evaluateJavascript(
                                        """document.getElementById('chapter_$index')?.scrollIntoView({behavior:'smooth'});""",
                                        null
                                    )
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. $chapterTitleItem",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (index * totalPages / allChapters.size.coerceAtLeast(1) in (currentPage - 1)..(currentPage + 1))
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            if (index * totalPages / allChapters.size.coerceAtLeast(1) == currentPage) {
                                Text("当前", style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                        }
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTocDialog = false }) { Text("关闭") }
            }
        )
    }
}

// ── Cover Page Overlay (Compose, shown before WebView) ─────────────────
@Composable
private fun CoverPageOverlay(
    coverBitmap: android.graphics.Bitmap?,
    title: String,
    authors: List<String>?,
    chapterCount: Int,
    currentTheme: EpubReaderTheme,
    onStartReading: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(currentTheme.backgroundColor)))
            .clickable { onStartReading() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            if (coverBitmap != null) {
                Image(
                    painter = BitmapPainter(coverBitmap.asImageBitmap()),
                    contentDescription = "封面",
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(0.7f)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .aspectRatio(0.7f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无封面", color = Color.Gray, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = currentTheme.textColorCompose,
                textAlign = TextAlign.Center
            )

            if (authors?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = authors.joinToString(" / "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = currentTheme.textColorCompose.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "共 $chapterCount 章",
                style = MaterialTheme.typography.bodyMedium,
                color = currentTheme.textColorCompose.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = currentTheme.textColorCompose.copy(alpha = 0.15f)
            ) {
                Text(
                    text = "点击开始阅读",
                    style = MaterialTheme.typography.bodyLarge,
                    color = currentTheme.textColorCompose,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp)
                )
            }
        }
    }
}

// ── Bottom Sheet Settings (掌阅-style) ─────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpubSettingsBottomSheet(
    fontSize: Float,
    lineSpacing: Float,
    theme: String,
    fontFamily: String,
    textJustify: Boolean,
    brightnessDim: Float,
    onFontSizeChange: (Float) -> Unit,
    onLineSpacingChange: (Float) -> Unit,
    onThemeChange: (String) -> Unit,
    onFontFamilyChange: (String) -> Unit,
    onTextJustifyChange: (Boolean) -> Unit,
    onBrightnessDimChange: (Float) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // ── Title ──
            Text("阅读设置", style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 20.dp))

            // ── Brightness ──
            Text("亮度", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.BrightnessMedium, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Slider(
                    value = brightnessDim,
                    onValueChange = onBrightnessDimChange,
                    valueRange = 0f..1f,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.BrightnessMedium, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // ── Font Size ──
            Text("字体大小: ${fontSize.toInt()}", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledIconButton(
                    onClick = { if (fontSize > 12f) onFontSizeChange(fontSize - 1) },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text("A-", fontSize = 14.sp) }

                Slider(
                    value = fontSize,
                    onValueChange = onFontSizeChange,
                    valueRange = 12f..28f,
                    steps = 15,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )

                FilledIconButton(
                    onClick = { if (fontSize < 28f) onFontSizeChange(fontSize + 1) },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) { Text("A+", fontSize = 14.sp) }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // ── Themes ──
            Text("主题", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                epubReaderThemes.forEach { rTheme ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { onThemeChange(rTheme.name) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(android.graphics.Color.parseColor(rTheme.backgroundColor)))
                                .then(
                                    if (theme == rTheme.name) Modifier.border(
                                        2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)
                                    ) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Aa", color = Color(android.graphics.Color.parseColor(rTheme.textColor)),
                                fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(rTheme.label, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // ── Line Spacing ──
            Text("行距: ${"%.1f".format(lineSpacing)}x", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            Slider(
                value = lineSpacing,
                onValueChange = onLineSpacingChange,
                valueRange = 1.0f..2.5f,
                steps = 14
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // ── Font Family ──
            Text("字体", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("sans-serif" to "默认", "serif" to "衬线", "monospace" to "等宽").forEach { (font, label) ->
                    FilterChip(
                        selected = fontFamily == font,
                        onClick = { onFontFamilyChange(font) },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // ── Text Justify ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTextJustifyChange(!textJustify) }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("两端对齐", style = MaterialTheme.typography.bodyMedium)
                Checkbox(checked = textJustify, onCheckedChange = onTextJustifyChange)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Apply / Cancel ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取消")
                }
                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("确定")
                }
            }
        }
    }
}
