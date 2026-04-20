package com.example.ireader.ui.reader

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.*
import java.io.File

private const val TAG = "ReaderScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(bookId: String, navController: NavController) {
    val context = LocalContext.current
    val viewModel: ReaderViewModel = viewModel()
    
    // 阅读设置
    val settingsManager = remember { ReadingSettingsManager(context) }
    var readingPrefs by remember { mutableStateOf(settingsManager.loadReadingPreferences()) }
    
    // 底部设置面板
    var showSettingsPanel by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    val book = viewModel.getBook(bookId)
    
    if (book == null) {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("无法加载书籍")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) { Text("返回") }
            }
        }
        return
    }
    
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }
    var pdfState by remember { mutableStateOf<PdfRenderState?>(null) }
    var epubContent by remember { mutableStateOf<List<SpineItem>>(emptyList()) }
    var currentSpineIndex by remember { mutableStateOf(0) }
    var showText by remember { mutableStateOf(false) }
    var textContent by remember { mutableStateOf("") }
    var txtPages by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentTxtPage by remember { mutableStateOf(0) }
    
    // 根据主题获取颜色
    val bgColor = when (readingPrefs.theme) {
        ReadingTheme.LIGHT -> Color.White
        ReadingTheme.DARK -> Color(0xFF1A1A1A)
        ReadingTheme.SEPIA -> Color(0xFFF5F0E6)
        ReadingTheme.GREEN -> Color(0xFFE8F5E9)
    }
    
    val textColor = when (readingPrefs.theme) {
        ReadingTheme.LIGHT -> Color.Black
        ReadingTheme.DARK -> Color.White
        ReadingTheme.SEPIA -> Color(0xFF5D4037)
        ReadingTheme.GREEN -> Color(0xFF2E7D32)
    }
    
    LaunchedEffect(book.id) {
        isLoading = true
        error = ""
        
        try {
            val uri = Uri.parse(book.filePath)
            try {
                context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } catch (e: Exception) { Log.w(TAG, "Permission: ${e.message}") }
            
            when (book.format.lowercase()) {
                "pdf" -> {
                    val pdfFile = copyToCache(context, uri, "${book.id}.pdf")
                    if (pdfFile == null) {
                        error = "无法访问 PDF 文件"
                        isLoading = false
                    } else {
                        val result = withTimeoutOrNull(60000L) {
                            withContext(Dispatchers.IO) {
                                val pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
                                val renderer = PdfRenderer(pfd)
                                val pages = renderer.pageCount
                                val firstPage = renderPageSync(renderer, book.lastReadPage, context)
                                PdfRenderState(renderer, pfd, pages, book.lastReadPage, firstPage)
                            }
                        }
                        if (result == null) {
                            error = "PDF 加载超时"
                            isLoading = false
                        } else {
                            pdfState = result
                            isLoading = false
                        }
                    }
                }
                "epub" -> {
                    epubContent = parseEpub(context, uri)
                    isLoading = false
                }
                "txt", "json" -> {
                    showText = true
                    textContent = loadText(context, uri)
                    // 分页处理
                    txtPages = splitTextToPages(textContent, 3000) // 每页约3000字符
                    isLoading = false
                }
                else -> {
                    error = "不支持: ${book.format}"
                    isLoading = false
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "加载错误: ${e.message}", e)
            error = "加载失败: ${e.message}"
            isLoading = false
        }
    }
    
    LaunchedEffect(pdfState?.currentPage) {
        val state = pdfState
        if (state != null && state.renderer != null) {
            val result = withTimeoutOrNull(15000L) {
                withContext(Dispatchers.IO) {
                    renderPageSync(state.renderer!!, state.currentPage, context)
                }
            }
            if (result != null) {
                pdfState = state.copy(currentBitmap = result)
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            pdfState?.renderer?.close()
            pdfState?.parcelFileDescriptor?.close()
        }
    }
    
    // 底部设置面板
    if (showSettingsPanel) {
        ModalBottomSheet(
            onDismissRequest = { showSettingsPanel = false },
            sheetState = sheetState
        ) {
            SettingsPanelContent(
                currentTheme = readingPrefs.theme,
                currentFontSize = readingPrefs.fontSize,
                isPdf = book.format.lowercase() == "pdf",
                onThemeChanged = { newTheme ->
                    readingPrefs = readingPrefs.copy(theme = newTheme)
                    settingsManager.saveReadingPreferences(readingPrefs)
                },
                onFontSizeIncrease = {
                    if (readingPrefs.fontSize < 24) {
                        readingPrefs = readingPrefs.copy(fontSize = readingPrefs.fontSize + 2)
                        settingsManager.saveReadingPreferences(readingPrefs)
                    }
                },
                onFontSizeDecrease = {
                    if (readingPrefs.fontSize > 12) {
                        readingPrefs = readingPrefs.copy(fontSize = readingPrefs.fontSize - 2)
                        settingsManager.saveReadingPreferences(readingPrefs)
                    }
                }
            )
        }
    }
    
    Column(Modifier.fillMaxSize().background(bgColor)) {
        TopAppBar(
            title = { Text(book.title, maxLines = 1, color = textColor) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回", tint = textColor)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
        )
        
        when {
            isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            error.isNotEmpty() -> Box(Modifier.fillMaxSize().padding(16.dp), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(error, color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) { Text("返回书架") }
                }
            }
            pdfState != null -> PdfViewer(
                state = pdfState!!, 
                bgColor = bgColor,
                onTapCenter = { 
                    Log.d(TAG, "onTapCenter called")
                    showSettingsPanel = true 
                },
                onPageChange = { page -> pdfState = pdfState?.copy(currentPage = page) }
            )
            epubContent.isNotEmpty() -> EpubViewer(
                content = epubContent, 
                index = currentSpineIndex, 
                bgColor = bgColor,
                textColor = textColor,
                fontSize = readingPrefs.fontSize,
                onTapCenter = { 
                    Log.d(TAG, "EpubViewer onTapCenter called")
                    showSettingsPanel = true 
                },
                onIndexChange = { currentSpineIndex = it }
            )
            showText && txtPages.isNotEmpty() -> TxtViewer(
                pages = txtPages,
                currentPage = currentTxtPage,
                bgColor = bgColor,
                textColor = textColor,
                fontSize = readingPrefs.fontSize,
                onTapCenter = { 
                    Log.d(TAG, "TxtViewer onTapCenter called")
                    showSettingsPanel = true 
                },
                onPageChange = { currentTxtPage = it }
            )
            showText -> TxtScrollViewer(
                content = textContent,
                bgColor = bgColor,
                textColor = textColor,
                fontSize = readingPrefs.fontSize,
                onTapCenter = { showSettingsPanel = true }
            )
        }
    }
}

// 将文本分割成多页
private fun splitTextToPages(text: String, charsPerPage: Int): List<String> {
    if (text.length <= charsPerPage) return listOf(text)
    
    val pages = mutableListOf<String>()
    var start = 0
    
    while (start < text.length) {
        var end = minOf(start + charsPerPage, text.length)
        
        // 尝试在句子结尾处分割
        if (end < text.length) {
            val searchStart = maxOf(start + charsPerPage - 200, start)
            val searchEnd = minOf(start + charsPerPage + 200, text.length)
            val segment = text.substring(searchStart, searchEnd)
            
            // 寻找句号、换行等自然分割点
            val breakPoints = listOf('\n', '\n', '.', '!', '?', '。', '！', '？')
            for (bp in breakPoints) {
                val idx = segment.lastIndexOf(bp)
                if (idx >= 0) {
                    end = searchStart + idx + 1
                    break
                }
            }
        }
        
        pages.add(text.substring(start, end))
        start = end
    }
    
    return pages
}

@Composable
private fun TxtViewer(
    pages: List<String>,
    currentPage: Int,
    bgColor: Color,
    textColor: Color,
    fontSize: Int,
    onTapCenter: () -> Unit,
    onPageChange: (Int) -> Unit
) {
    // 滑动阈值
    val swipeThreshold = 100f
    var dragOffset by remember { mutableStateOf(0f) }
    
    Box(Modifier.fillMaxSize().background(bgColor)) {
        // 文本内容
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                pages[currentPage],
                color = textColor,
                fontSize = fontSize.sp,
                lineHeight = (fontSize + 4).sp
            )
        }
        
        // 透明覆盖层用于手势检测（点击 + 滑动）
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x01000000))
                .pointerInput(pages, currentPage) {
                    // 垂直滑动翻页
                    detectVerticalDragGestures(
                        onDragEnd = {
                            Log.d(TAG, "TXT drag end: offset=$dragOffset, threshold=$swipeThreshold")
                            if (dragOffset > swipeThreshold && currentPage < pages.size - 1) {
                                // 向下滑动 = 下一页
                                onPageChange(currentPage + 1)
                            } else if (dragOffset < -swipeThreshold && currentPage > 0) {
                                // 向上滑动 = 上一页
                                onPageChange(currentPage - 1)
                            }
                            dragOffset = 0f
                        },
                        onVerticalDrag = { _, dragAmount ->
                            dragOffset += dragAmount
                        }
                    )
                }
                .pointerInput(Unit) {
                    // 点击手势
                    detectTapGestures(onTap = { offset ->
                        Log.d(TAG, "TXT tap at offset: $offset, width: ${size.width}")
                        val width = size.width
                        when {
                            offset.x < width / 3 && currentPage > 0 -> {
                                onPageChange(currentPage - 1)
                            }
                            offset.x >= width * 2 / 3 && currentPage < pages.size - 1 -> {
                                onPageChange(currentPage + 1)
                            }
                            else -> {
                                onTapCenter()
                            }
                        }
                    })
                }
        )
        
        // 页码指示（上下箭头）
        if (pages.size > 1) {
            Box(Modifier.fillMaxSize().padding(bottom = 16.dp), Alignment.BottomCenter) {
                Surface(shape = MaterialTheme.shapes.small, color = bgColor.copy(alpha = 0.9f)) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton({ if (currentPage > 0) onPageChange(currentPage - 1) }, enabled = currentPage > 0) {
                            Icon(Icons.Filled.KeyboardArrowUp, "上一页", tint = textColor)
                        }
                        Text("${currentPage + 1} / ${pages.size}", color = textColor)
                        IconButton({ if (currentPage < pages.size - 1) onPageChange(currentPage + 1) }, enabled = currentPage < pages.size - 1) {
                            Icon(Icons.Filled.KeyboardArrowDown, "下一页", tint = textColor)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TxtScrollViewer(
    content: String,
    bgColor: Color,
    textColor: Color,
    fontSize: Int,
    onTapCenter: () -> Unit
) {
    Box(Modifier.fillMaxSize().background(bgColor)) {
        // 文本内容
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(content, color = textColor, fontSize = fontSize.sp)
        }
        
        // 透明覆盖层用于手势检测
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x01000000))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { offset ->
                        val width = size.width
                        if (offset.x >= width / 3 && offset.x < width * 2 / 3) {
                            onTapCenter()
                        }
                    })
                }
        )
    }
}

@Composable
private fun SettingsPanelContent(
    currentTheme: ReadingTheme,
    currentFontSize: Int,
    isPdf: Boolean,
    onThemeChanged: (ReadingTheme) -> Unit,
    onFontSizeIncrease: () -> Unit,
    onFontSizeDecrease: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("阅读设置", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        
        // 主题选择
        Text("主题", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
            ThemeButton(ReadingTheme.LIGHT, currentTheme, onThemeChanged, "日间")
            ThemeButton(ReadingTheme.DARK, currentTheme, onThemeChanged, "夜间")
            ThemeButton(ReadingTheme.SEPIA, currentTheme, onThemeChanged, "护眼")
            ThemeButton(ReadingTheme.GREEN, currentTheme, onThemeChanged, "绿色")
        }
        
        if (!isPdf) {
            Spacer(Modifier.height(16.dp))
            Text("字体大小: ${currentFontSize}sp", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                FilledIconButton(onClick = onFontSizeDecrease, enabled = currentFontSize > 12) {
                    Icon(Icons.Filled.Remove, "减小")
                }
                Spacer(Modifier.width(16.dp))
                FilledIconButton(onClick = onFontSizeIncrease, enabled = currentFontSize < 24) {
                    Icon(Icons.Filled.Add, "增大")
                }
            }
        } else {
            Spacer(Modifier.height(16.dp))
            Text("PDF 文件不支持字体调整", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ThemeButton(
    theme: ReadingTheme,
    currentTheme: ReadingTheme,
    onClick: (ReadingTheme) -> Unit,
    label: String
) {
    val isSelected = theme == currentTheme
    val bgColor = when (theme) {
        ReadingTheme.LIGHT -> Color.White
        ReadingTheme.DARK -> Color(0xFF1A1A1A)
        ReadingTheme.SEPIA -> Color(0xFFF5F0E6)
        ReadingTheme.GREEN -> Color(0xFFE8F5E9)
    }
    
    Column(
        Modifier
            .clickable { onClick(theme) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = MaterialTheme.shapes.small,
            color = bgColor,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
        ) {}
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun PdfViewer(
    state: PdfRenderState,
    bgColor: Color,
    onTapCenter: () -> Unit,
    onPageChange: (Int) -> Unit
) {
    var isRendering by remember { mutableStateOf(false) }
    
    Box(Modifier.fillMaxSize().background(bgColor)) {
        // PDF 内容
        if (state.currentBitmap != null && !isRendering) {
            Image(
                bitmap = state.currentBitmap.asImageBitmap(),
                contentDescription = "PDF 页面",
                modifier = Modifier.fillMaxSize()
            )
        }
        
        // 透明覆盖层用于手势检测
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x01000000))
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { offset ->
                        Log.d(TAG, "PDF tap at offset: $offset, width: ${size.width}")
                        val width = size.width
                        when {
                            offset.x < width / 3 && state.currentPage > 0 -> {
                                onPageChange(state.currentPage - 1)
                                isRendering = true
                            }
                            offset.x >= width * 2 / 3 && state.currentPage < state.totalPages - 1 -> {
                                onPageChange(state.currentPage + 1)
                                isRendering = true
                            }
                            else -> {
                                onTapCenter()
                            }
                        }
                    })
                }
        )
        
        if (isRendering || state.currentBitmap == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        }
        
        // 页码指示
        if (state.totalPages > 0 && state.currentBitmap != null) {
            Box(Modifier.fillMaxSize().padding(bottom = 16.dp), Alignment.BottomCenter) {
                Surface(shape = MaterialTheme.shapes.small, color = bgColor.copy(alpha = 0.9f)) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton({ if (state.currentPage > 0) { onPageChange(state.currentPage - 1); isRendering = true } }, enabled = state.currentPage > 0) {
                            Icon(Icons.Filled.KeyboardArrowLeft, "上一页")
                        }
                        Text("${state.currentPage + 1} / ${state.totalPages}")
                        IconButton({ if (state.currentPage < state.totalPages - 1) { onPageChange(state.currentPage + 1); isRendering = true } }, enabled = state.currentPage < state.totalPages - 1) {
                            Icon(Icons.Filled.KeyboardArrowRight, "下一页")
                        }
                    }
                }
            }
        }
    }
    
    LaunchedEffect(state.currentBitmap) { if (state.currentBitmap != null) isRendering = false }
}

@Composable
private fun EpubViewer(
    content: List<SpineItem>,
    index: Int,
    bgColor: Color,
    textColor: Color,
    fontSize: Int,
    onTapCenter: () -> Unit,
    onIndexChange: (Int) -> Unit
) {
    Column(Modifier.fillMaxSize().background(bgColor)) {
        Box(Modifier.weight(1f)) {
            // WebView 内容
            AndroidView(
                factory = { ctx -> 
                    WebView(ctx).apply { 
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    }
                },
                update = { view -> 
                    val cssColor = when(textColor) {
                        Color.White -> "#FFFFFF"
                        Color.Black -> "#000000"
                        else -> "#5D4037"
                    }
                    val css = """
                        <style>
                            body { 
                                background-color: transparent;
                                color: $cssColor;
                                font-size: ${fontSize}px;
                                padding: 16px;
                            }
                        </style>
                    """
                    view.loadDataWithBaseURL(null, css + content[index].content, "text/html", "UTF-8", null)
                }
            )
            
            // 透明覆盖层用于手势检测
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color(0x01000000))
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { offset ->
                            Log.d(TAG, "EPUB tap at offset: $offset, width: ${size.width}")
                            val width = size.width
                            if (offset.x >= width / 3 && offset.x < width * 2 / 3) {
                                onTapCenter()
                            }
                        })
                    }
            )
        }
        
        // 翻页控制
        Row(Modifier.fillMaxWidth().padding(8.dp), Arrangement.SpaceBetween) {
            IconButton({ if (index > 0) onIndexChange(index - 1) }, enabled = index > 0) { 
                Icon(Icons.Filled.KeyboardArrowLeft, "上一章") 
            }
            Text("${index + 1} / ${content.size}")
            IconButton({ if (index < content.size - 1) onIndexChange(index + 1) }, enabled = index < content.size - 1) { 
                Icon(Icons.Filled.KeyboardArrowRight, "下一章") 
            }
        }
    }
}

// 数据类
data class PdfRenderState(
    val renderer: PdfRenderer?, 
    val parcelFileDescriptor: ParcelFileDescriptor?, 
    val totalPages: Int, 
    val currentPage: Int, 
    val currentBitmap: Bitmap?,
    val zoom: Float = 1.0f
)

data class SpineItem(val id: String, val href: String, val content: String)

// 辅助函数
private fun renderPageSync(renderer: PdfRenderer, pageIndex: Int, context: Context): Bitmap {
    val page = renderer.openPage(pageIndex)
    val width = context.resources.displayMetrics.widthPixels
    val height = (width * page.height / page.width)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.eraseColor(android.graphics.Color.WHITE)
    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()
    return bitmap
}

private fun copyToCache(context: Context, uri: Uri, fileName: String): File? {
    return try {
        val cacheFile = File(context.cacheDir, fileName)
        context.contentResolver.openInputStream(uri)?.use { input -> 
            cacheFile.outputStream().use { output -> input.copyTo(output) } 
        }
        cacheFile
    } catch (e: Exception) { null }
}

private fun parseEpub(context: Context, uri: Uri): List<SpineItem> {
    return context.contentResolver.openInputStream(uri)?.use { stream ->
        val zip = java.util.zip.ZipInputStream(stream)
        val items = mutableListOf<SpineItem>()
        var entry = zip.nextEntry
        while (entry != null) {
            if (entry.name.lowercase().matches(Regex(".+\\.html?"))) {
                items.add(SpineItem(entry.name, entry.name, BufferedReader(InputStreamReader(zip)).readText()))
            }
            entry = zip.nextEntry
        }
        items
    } ?: emptyList()
}

private fun loadText(context: Context, uri: Uri): String {
    return context.contentResolver.openInputStream(uri)?.use { 
        BufferedReader(InputStreamReader(it, "UTF-8")).readText() 
    } ?: ""
}