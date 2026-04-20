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
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
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
    var currentSpineIndex by remember { mutableStateOf(book.lastReadChapter) }  // 恢复章节
    var showText by remember { mutableStateOf(false) }
    var textContent by remember { mutableStateOf("") }
    var txtPages by remember { mutableStateOf<List<String>>(emptyList()) }
    var currentTxtPage by remember { mutableStateOf(book.lastReadPage) }  // 恢复页码
    
    // 恢复阅读模式
    LaunchedEffect(book.id) {
        try {
            val savedMode = ReadingMode.valueOf(book.lastReadMode)
            readingPrefs = readingPrefs.copy(readingMode = savedMode)
        } catch (e: Exception) {
            // 默认翻页模式
        }
        // 恢复字体大小
        if (book.lastFontSize in 12..24) {
            readingPrefs = readingPrefs.copy(fontSize = book.lastFontSize)
            settingsManager.saveReadingPreferences(readingPrefs)
        }
    }
    
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
                                val firstPage = renderPageSync(renderer, book.lastReadPage, context, book.lastZoom)
                                PdfRenderState(renderer, pfd, pages, book.lastReadPage, firstPage, book.lastZoom)
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
    
    // 监听页码和缩放变化，重新渲染页面
    LaunchedEffect(pdfState?.currentPage, pdfState?.zoom) {
        val state = pdfState
        if (state != null && state.renderer != null) {
            val result = withTimeoutOrNull(15000L) {
                withContext(Dispatchers.IO) {
                    renderPageSync(state.renderer!!, state.currentPage, context, state.zoom)
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
            
            // 退出时保存阅读进度
            val page = pdfState?.currentPage ?: currentTxtPage
            val chapter = currentSpineIndex
            val progress = if (pdfState != null) {
                // PDF：页码进度（页码+1因为从0开始）
                (((page + 1).toFloat() / (pdfState?.totalPages ?: 1)) * 100).toInt()
            } else if (epubContent.isNotEmpty()) {
                // EPUB：章节进度（索引+1因为从0开始）
                (((currentSpineIndex + 1).toFloat() / epubContent.size) * 100).toInt()
            } else if (txtPages.isNotEmpty()) {
                // TXT：页码进度（页码+1因为从0开始）
                (((currentTxtPage + 1).toFloat() / txtPages.size) * 100).toInt()
            } else {
                book.progress
            }
            
            viewModel.saveReadProgress(
                bookId = book.id,
                page = page,
                chapter = chapter,
                progress = progress,
                readingMode = readingPrefs.readingMode.name,
                scrollPosition = 0,
                fontSize = readingPrefs.fontSize,
                zoom = pdfState?.zoom ?: 1.0f
            )
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
                currentReadingMode = readingPrefs.readingMode,
                currentZoom = pdfState?.zoom ?: 1.0f,
                isPdf = book.format.lowercase() == "pdf",
                onThemeChanged = { newTheme ->
                    readingPrefs = readingPrefs.copy(theme = newTheme)
                    settingsManager.saveReadingPreferences(readingPrefs)
                },
                onReadingModeChanged = { newMode ->
                    readingPrefs = readingPrefs.copy(readingMode = newMode)
                    settingsManager.saveReadingPreferences(readingPrefs)
                    // 保存阅读模式到数据库
                    viewModel.updateReadProgress(
                        bookId = book.id,
                        readingMode = newMode.name,
                        fontSize = readingPrefs.fontSize
                    )
                },
                onFontSizeChanged = { newSize ->
                    if (newSize != readingPrefs.fontSize) {
                        readingPrefs = readingPrefs.copy(fontSize = newSize)
                        settingsManager.saveReadingPreferences(readingPrefs)
                    }
                },
                onZoomChanged = { newZoom ->
                    if (pdfState != null) {
                        pdfState = pdfState?.copy(zoom = newZoom)
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
                onPageChange = { page -> pdfState = pdfState?.copy(currentPage = page) },
                onZoomChange = { newZoom -> pdfState = pdfState?.copy(zoom = newZoom) }
            )
            epubContent.isNotEmpty() -> EpubViewer(
                content = epubContent, 
                index = currentSpineIndex, 
                bgColor = bgColor,
                textColor = textColor,
                fontSize = readingPrefs.fontSize,
                readingMode = readingPrefs.readingMode,
                onTapCenter = { 
                    Log.d(TAG, "EpubViewer onTapCenter called")
                    showSettingsPanel = true 
                },
                onIndexChange = { currentSpineIndex = it }
            )
            // 根据阅读模式选择显示方式
            showText && readingPrefs.readingMode == ReadingMode.SCROLL -> TxtScrollViewer(
                content = textContent,
                bgColor = bgColor,
                textColor = textColor,
                fontSize = readingPrefs.fontSize,
                bookId = book.id,
                onTapCenter = { showSettingsPanel = true }
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
                bookId = book.id,
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
                            // 拖拽式翻页：手指向下拖=内容下移=露出上方=上一页，手指向上拖=内容上移=露出下方=下一页
                            if (dragOffset > swipeThreshold && currentPage > 0) {
                                // 向下滑动（手指向下拖）= 上一页
                                onPageChange(currentPage - 1)
                            } else if (dragOffset < -swipeThreshold && currentPage < pages.size - 1) {
                                // 向上滑动（手指向上拖）= 下一页
                                onPageChange(currentPage + 1)
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
    bookId: String,
    onTapCenter: () -> Unit
) {
    val context = LocalContext.current
    val scrollPrefs = remember { context.getSharedPreferences("scroll_positions", Context.MODE_PRIVATE) }
    val scrollState = rememberScrollState()
    
    // 恢复滚动位置
    LaunchedEffect(bookId) {
        val savedPosition = scrollPrefs.getInt(bookId, 0)
        scrollState.scrollTo(savedPosition)
    }
    
    // 保存滚动位置（滚动停止时）
    LaunchedEffect(scrollState.value) {
        if (!scrollState.isScrollInProgress) {
            scrollPrefs.edit { putInt(bookId, scrollState.value) }
        }
    }
    
    Box(Modifier.fillMaxSize().background(bgColor)) {
        // 文本内容（可滚动）
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
                .pointerInput(Unit) {
                    // 只检测点击中央区域
                    detectTapGestures(onTap = { offset ->
                        val width = size.width
                        if (offset.x >= width / 3 && offset.x < width * 2 / 3) {
                            onTapCenter()
                        }
                    })
                }
        ) {
            Text(content, color = textColor, fontSize = fontSize.sp, lineHeight = (fontSize + 4).sp)
        }
        
        // 滚动进度指示（右上角）
        if (scrollState.maxValue > 0) {
            val progress = (scrollState.value.toFloat() / scrollState.maxValue * 100).toInt()
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, end = 8.dp),
                Alignment.TopEnd
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = bgColor.copy(alpha = 0.8f)
                ) {
                    Text(
                        "${progress}%",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = textColor
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsPanelContent(
    currentTheme: ReadingTheme,
    currentFontSize: Int,
    currentReadingMode: ReadingMode,
    currentZoom: Float,
    isPdf: Boolean,
    onThemeChanged: (ReadingTheme) -> Unit,
    onReadingModeChanged: (ReadingMode) -> Unit,
    onFontSizeChanged: (Int) -> Unit,
    onZoomChanged: (Float) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("阅读设置", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        
        // PDF 缩放滑块
        if (isPdf) {
            Text("页面缩放: ${(currentZoom * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Slider(
                value = currentZoom,
                onValueChange = onZoomChanged,
                valueRange = 0.5f..3.0f,
                steps = 50,
                modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("50%", style = MaterialTheme.typography.labelSmall)
                Text("100%", style = MaterialTheme.typography.labelSmall)
                Text("300%", style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(16.dp))
        }
        
        // 阅读模式选择
        Text("阅读模式", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
            ModeButton(ReadingMode.PAGED, currentReadingMode, onReadingModeChanged, "翻页")
            ModeButton(ReadingMode.SCROLL, currentReadingMode, onReadingModeChanged, "滚动")
        }
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
            Slider(
                value = currentFontSize.toFloat(),
                onValueChange = { newSize -> onFontSizeChanged(newSize.toInt()) },
                valueRange = 12f..24f,
                steps = 6,
                modifier = Modifier.fillMaxWidth()
            )
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("12sp", style = MaterialTheme.typography.labelSmall)
                Text("18sp", style = MaterialTheme.typography.labelSmall)
                Text("24sp", style = MaterialTheme.typography.labelSmall)
            }
        }
        
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ModeButton(
    mode: ReadingMode,
    currentMode: ReadingMode,
    onClick: (ReadingMode) -> Unit,
    label: String
) {
    val isSelected = mode == currentMode
    
    Column(
        Modifier
            .clickable { onClick(mode) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = MaterialTheme.shapes.small,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Icon(
                    imageVector = if (mode == ReadingMode.PAGED) Icons.Filled.MenuBook else Icons.Filled.ViewStream,
                    contentDescription = label,
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
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
    onPageChange: (Int) -> Unit,
    onZoomChange: (Float) -> Unit
) {
    var isRendering by remember { mutableStateOf(false) }
    val swipeThreshold = 100f
    var dragOffset by remember { mutableStateOf(0f) }
    
    // 缩放状态 - 使用graphicsLayer实时缩放，从state.zoom恢复
    var scale by remember(state.zoom) { mutableStateOf(state.zoom) }
    
    Box(
        Modifier
            .fillMaxSize()
            .background(bgColor)
            .clipToBounds()
    ) {
        // PDF 内容 - 使用 graphicsLayer 进行实时缩放变换
        if (state.currentBitmap != null && !isRendering) {
            Image(
                bitmap = state.currentBitmap.asImageBitmap(),
                contentDescription = "PDF 页面",
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale
                    )
                    .wrapContentSize(Alignment.Center)
            )
        }
        
        // 透明覆盖层用于手势检测
        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    // 双指缩放手势
                    detectTransformGestures { _, pan, zoomChange, rotation ->
                        val newScale = (scale * zoomChange).coerceIn(0.5f, 3.0f)
                        scale = newScale
                        onZoomChange(newScale)
                    }
                }
                .pointerInput(state.totalPages, state.currentPage) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (dragOffset > swipeThreshold && state.currentPage > 0) {
                                onPageChange(state.currentPage - 1)
                                isRendering = true
                            } else if (dragOffset < -swipeThreshold && state.currentPage < state.totalPages - 1) {
                                onPageChange(state.currentPage + 1)
                                isRendering = true
                            }
                            dragOffset = 0f
                        },
                        onVerticalDrag = { _, dragAmount ->
                            dragOffset += dragAmount
                        }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { offset ->
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
        
        // 页码指示（上下箭头）+ 缩放百分比
        if (state.totalPages > 0 && state.currentBitmap != null) {
            Box(Modifier.fillMaxSize().padding(bottom = 16.dp), Alignment.BottomCenter) {
                Surface(shape = MaterialTheme.shapes.small, color = bgColor.copy(alpha = 0.9f)) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton({ if (state.currentPage > 0) { onPageChange(state.currentPage - 1); isRendering = true } }, enabled = state.currentPage > 0) {
                            Icon(Icons.Filled.KeyboardArrowUp, "上一页")
                        }
                        Text("${state.currentPage + 1} / ${state.totalPages}  ${(scale * 100).toInt()}%")
                        IconButton({ if (state.currentPage < state.totalPages - 1) { onPageChange(state.currentPage + 1); isRendering = true } }, enabled = state.currentPage < state.totalPages - 1) {
                            Icon(Icons.Filled.KeyboardArrowDown, "下一页")
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
    readingMode: ReadingMode,
    onTapCenter: () -> Unit,
    onIndexChange: (Int) -> Unit
) {
    val swipeThreshold = 100f
    var dragOffset by remember { mutableStateOf(0f) }
    
    Box(Modifier.fillMaxSize().background(bgColor)) {
        // WebView 内容
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx -> 
                WebView(ctx).apply { 
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    settings.setSupportZoom(false)
                    settings.builtInZoomControls = false
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    // 启用原生滚动
                    setVerticalScrollBarEnabled(true)
                    setHorizontalScrollBarEnabled(false)
                    scrollBarStyle = WebView.SCROLLBARS_INSIDE_OVERLAY
                    overScrollMode = WebView.OVER_SCROLL_NEVER
                }
            },
            update = { view -> 
                val cssColor = when(textColor) {
                    Color.White -> "#FFFFFF"
                    Color.Black -> "#000000"
                    else -> "#5D4037"
                }
                val scrollStyle = if (readingMode == ReadingMode.SCROLL) {
                    "overflow-y: scroll; overflow-x: hidden; height: auto;"
                } else {
                    "overflow: hidden;"
                }
                val htmlContent = """
                    <!DOCTYPE html>
                    <html>
                    <head><style>
                    html { height: 100%; }
                    body { 
                        background-color: transparent;
                        color: $cssColor;
                        font-size: ${fontSize}px;
                        padding: 16px;
                        margin: 0;
                        min-height: 100%;
                        $scrollStyle
                    }
                    </style></head>
                    <body>${content[index].content}</body>
                    </html>
                """
                view.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
            }
        )
        
        // 翻页模式：覆盖层拦截手势
        if (readingMode == ReadingMode.PAGED) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .pointerInput(content.size, index) {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                Log.d(TAG, "EPUB PAGED drag end: offset=$dragOffset")
                                if (dragOffset > swipeThreshold && index > 0) {
                                    onIndexChange(index - 1)
                                } else if (dragOffset < -swipeThreshold && index < content.size - 1) {
                                    onIndexChange(index + 1)
                                }
                                dragOffset = 0f
                            },
                            onVerticalDrag = { _, dragAmount ->
                                dragOffset += dragAmount
                            }
                        )
                    }
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { offset ->
                            val width = size.width
                            Log.d(TAG, "EPUB PAGED tap: x=${offset.x}, width=$width")
                            if (offset.x >= width / 3 && offset.x < width * 2 / 3) {
                                onTapCenter()
                            }
                        })
                    }
            )
            
            // 底部翻页控制
            Box(Modifier.fillMaxSize().padding(bottom = 16.dp), Alignment.BottomCenter) {
                Surface(shape = MaterialTheme.shapes.small, color = bgColor.copy(alpha = 0.9f)) {
                    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconButton({ if (index > 0) onIndexChange(index - 1) }, enabled = index > 0) { 
                            Icon(Icons.Filled.KeyboardArrowUp, "上一章", tint = textColor) 
                        }
                        Text("${index + 1} / ${content.size}", color = textColor)
                        IconButton({ if (index < content.size - 1) onIndexChange(index + 1) }, enabled = index < content.size - 1) { 
                            Icon(Icons.Filled.KeyboardArrowDown, "下一章", tint = textColor) 
                        }
                    }
                }
            }
        }
        
        // 滚动模式：设置按钮（右上角悬浮）- 不覆盖 WebView
        if (readingMode == ReadingMode.SCROLL) {
            FloatingActionButton(
                onClick = onTapCenter,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(40.dp),
                shape = RoundedCornerShape(50),
                containerColor = bgColor.copy(alpha = 0.9f)
            ) {
                Icon(Icons.Filled.Settings, "设置", tint = textColor, modifier = Modifier.size(20.dp))
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
private fun renderPageSync(renderer: PdfRenderer, pageIndex: Int, context: Context, zoom: Float = 1.0f): Bitmap {
    val page = renderer.openPage(pageIndex)
    val baseWidth = context.resources.displayMetrics.widthPixels
    val width = (baseWidth * zoom).toInt()
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