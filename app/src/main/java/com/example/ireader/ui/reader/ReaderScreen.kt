package com.example.ireader.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ireader.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ReaderTheme(
    val name: String,
    val label: String,
    val backgroundColor: Color,
    val textColor: Color,
    val indicatorColor: Color
)

val readerThemes = listOf(
    ReaderTheme("light", "日间", Color.White, Color(0xFF333333), Color(0xFF1976D2)),
    ReaderTheme("sepia", "护眼", Color(0xFFF5F0E8), Color(0xFF5C4B37), Color(0xFF8D6E63)),
    ReaderTheme("dark", "夜间", Color(0xFF1A1A2E), Color(0xFFCCCCCC), Color(0xFF42A5F5)),
    ReaderTheme("green", "绿色", Color(0xFFE8F5E9), Color(0xFF33691E), Color(0xFF66BB6A))
)

val highlightColors = listOf(
    Color(0xFFFFF176) to "黄色",
    Color(0xFF81C784) to "绿色",
    Color(0xFF64B5F6) to "蓝色",
    Color(0xFFFF8A65) to "橙色"
)

private val HighlightColor = Color(0xFFFFF176)
private val HighlightTextColor = Color(0xFF333333)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    bookId: String,
    navController: NavController? = null,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsStateWithLifecycle()
    val isPdf by viewModel.isPdf.collectAsStateWithLifecycle()
    val isEpub by viewModel.isEpub.collectAsStateWithLifecycle()
    val epubInfo by viewModel.epubInfo.collectAsStateWithLifecycle()

    when {
        isPdf -> {
            PdfReaderScreen(
                filePath = book?.filePath,
                title = book?.title ?: "PDF阅读",
                onNavigateBack = { navController?.popBackStack() },
                onSaveProgress = { page, total ->
                    viewModel.saveReaderProgress(bookId, page, total)
                }
            )
        }
        isEpub && epubInfo != null -> {
            EpubReaderScreen(
                filePath = book?.filePath,
                bookId = bookId,
                title = book?.title ?: "EPUB阅读",
                epubInfo = epubInfo,
                onNavigateBack = { navController?.popBackStack() },
                onSaveProgress = { page, total ->
                    viewModel.saveReaderProgress(bookId, page, total)
                }
            )
        }
        else -> {
            TxtReaderContent(
                bookId = bookId,
                book = book,
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TxtReaderContent(
    bookId: String,
    book: com.example.ireader.data.local.entity.Book?,
    navController: NavController?,
    viewModel: ReaderViewModel
) {
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val currentChapter by viewModel.currentChapter.collectAsStateWithLifecycle()
    val showMenu by viewModel.showMenu.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showBookmarkDialog by remember { mutableStateOf(false) }
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    val lineSpacing by viewModel.lineSpacing.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    var showSettingsDialog by remember { mutableStateOf(false) }
    var dialogFontSize by remember { mutableStateOf(viewModel.fontSize.value) }
    var dialogLineSpacing by remember { mutableStateOf(viewModel.lineSpacing.value) }
    var dialogTheme by remember { mutableStateOf(viewModel.theme.value) }

    var showAnnotationDialog by remember { mutableStateOf(false) }
    var annotationText by remember { mutableStateOf("") }
    var annotationNote by remember { mutableStateOf("") }
    var annotationColor by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val currentTheme = readerThemes.find { it.name == theme } ?: readerThemes[0]
    val isBookmarked = viewModel.isChapterBookmarked(currentChapter)

    // Search state
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()
    var detectedChapter by remember { mutableIntStateOf(currentChapter) }

    // Auto-detect chapter from scroll
    LaunchedEffect(scrollState.value) {
        if (chapters.isNotEmpty() && chapters.size > 1) {
            val maxScroll = scrollState.maxValue.coerceAtLeast(1)
            val ratio = scrollState.value.toFloat() / maxScroll.toFloat()
            val newChapter = (ratio * chapters.size).toInt().coerceIn(0, chapters.lastIndex)
            if (newChapter != detectedChapter) {
                detectedChapter = newChapter
                viewModel.changeChapter(newChapter)
            }
        }
    }

    Scaffold(
        containerColor = currentTheme.backgroundColor,
        topBar = {
            if (!isSearchActive && showMenu) {
                TopAppBar(
                    title = { Text(book?.title ?: "阅读", color = currentTheme.textColor) },
                    navigationIcon = {
                        navController?.let { nc ->
                            IconButton(onClick = { nc.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回", tint = currentTheme.textColor)
                            }
                        }
                    },
                    actions = {
                        // Search button
                        IconButton(onClick = { viewModel.startSearch() }) {
                            Icon(Icons.Default.Search, contentDescription = "搜索", tint = currentTheme.textColor)
                        }
                        IconButton(onClick = {
                            if (isBookmarked) {
                                viewModel.removeBookmark(currentChapter)
                            } else {
                                viewModel.addBookmark(bookId, currentChapter)
                                showBookmarkDialog = true
                            }
                        }) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isBookmarked) "移除书签" else "添加书签",
                                tint = currentTheme.textColor
                            )
                        }
                        IconButton(onClick = {
                            annotationText = ""
                            annotationNote = ""
                            annotationColor = 0
                            showAnnotationDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "添加笔记",
                                tint = currentTheme.textColor
                            )
                        }
                        IconButton(onClick = {
                            navController?.navigate(Screen.Bookmarks.createRoute(bookId))
                        }) {
                            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "书签列表", tint = currentTheme.textColor)
                        }
                        IconButton(onClick = {
                            dialogFontSize = fontSize
                            dialogLineSpacing = lineSpacing
                            dialogTheme = theme
                            showSettingsDialog = true
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置", tint = currentTheme.textColor)
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(currentTheme.backgroundColor)
                .padding(padding)
        ) {
            if (isSearchActive) {
                // ═══ Search panel overlay ═══
                Column(modifier = Modifier.fillMaxSize()) {
                    // Search bar
                    Surface(
                        tonalElevation = 2.dp,
                        color = currentTheme.backgroundColor
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.stopSearch() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "关闭搜索", tint = currentTheme.textColor)
                            }
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.searchInBook(it) },
                                placeholder = { Text("搜索书中内容...", color = currentTheme.textColor.copy(alpha = 0.5f)) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = currentTheme.textColor,
                                    unfocusedTextColor = currentTheme.textColor,
                                    cursorColor = currentTheme.textColor,
                                    focusedBorderColor = currentTheme.textColor.copy(alpha = 0.5f),
                                    unfocusedBorderColor = currentTheme.textColor.copy(alpha = 0.2f)
                                )
                            )
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = {
                                    viewModel.searchInBook("")
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "清除", tint = currentTheme.textColor)
                                }
                            }
                        }
                    }

                    // Results summary
                    if (searchQuery.isNotBlank()) {
                        Surface(
                            color = currentTheme.backgroundColor.copy(alpha = 0.9f)
                        ) {
                            Text(
                                text = if (isSearching) "搜索中..." else "找到 ${searchResults.size} 处匹配",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = currentTheme.textColor.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Results list
                    if (searchResults.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(currentTheme.backgroundColor.copy(alpha = 0.95f))
                        ) {
                            itemsIndexed(searchResults) { _, result ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.goToSearchResult(result)
                                            viewModel.stopSearch()
                                            scope.launch {
                                                delay(100) // Wait for layout pass after search panel closes
                                                if (chapters.size > 1) {
                                                    val target = (scrollState.maxValue * result.chapterIndex / chapters.size).coerceAtLeast(0)
                                                    scrollState.animateScrollTo(target)
                                                }
                                            }
                                        },
                                    color = if (result.chapterIndex == currentChapter)
                                        currentTheme.indicatorColor.copy(alpha = 0.1f)
                                    else currentTheme.backgroundColor
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                                    ) {
                                        Text(
                                            text = "第${result.chapterIndex + 1}章",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = currentTheme.indicatorColor
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = buildHighlightedString(
                                                result.contextBefore + result.matchText + result.contextAfter,
                                                result.matchText,
                                                isActive = true
                                            ),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = currentTheme.textColor,
                                            maxLines = 2
                                        )
                                    }
                                }
                                HorizontalDivider(color = currentTheme.textColor.copy(alpha = 0.1f))
                            }
                        }
                    }
                }
            }

            // ═══ Main content ═══
            // Only show content when search is NOT active
            if (!isSearchActive) {
                if (chapters.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(showMenu) {
                                detectTapGestures(
                                    onTap = { offset ->
                                        val screenWidth = this.size.width
                                        if (offset.x > screenWidth * 0.3f && offset.x < screenWidth * 0.7f) {
                                            viewModel.toggleMenu()
                                        }
                                    }
                                )
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                        ) {
                            chapters.forEachIndexed { index, content ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.changeChapter(index)
                                            scope.launch {
                                                val target = if (chapters.size > 1) {
                                                    (scrollState.maxValue * index / chapters.size).coerceAtLeast(0)
                                                } else 0
                                                scrollState.animateScrollTo(target)
                                            }
                                        },
                                    color = currentTheme.backgroundColor.copy(alpha = 0.5f)
                                ) {
                                    Text(
                                        text = "第${index + 1}章",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 12.dp, horizontal = 16.dp),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = currentTheme.textColor.copy(alpha = 0.7f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                HorizontalDivider(color = currentTheme.textColor.copy(alpha = 0.15f))
                                ChapterContent(
                                    content = content,
                                    fontSize = fontSize,
                                    lineSpacing = lineSpacing,
                                    textColor = currentTheme.textColor,
                                    searchQuery = searchQuery,
                                    isSearchActive = false // don't highlight in main content
                                )
                                if (index < chapters.size - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(
                                        color = currentTheme.textColor.copy(alpha = 0.3f),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        // Page indicator overlay
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "${currentChapter + 1} / ${chapters.size}",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("暂无内容", color = currentTheme.textColor)
                    }
                }
            }
        }
    }

    // ── Dialogs ──────────────────────────────────────────────────────
    if (showBookmarkDialog) {
        LaunchedEffect(Unit) { showBookmarkDialog = false }
    }

    if (showAnnotationDialog) {
        AlertDialog(
            onDismissRequest = { showAnnotationDialog = false },
            title = { Text("添加笔记") },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = annotationText,
                        onValueChange = { annotationText = it },
                        label = { Text("高亮文字") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = annotationNote,
                        onValueChange = { annotationNote = it },
                        label = { Text("批注 (可选)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("颜色", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        itemsIndexed(highlightColors) { index, (color, label) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { annotationColor = index }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                            width = if (annotationColor == index) 3.dp else 0.dp,
                                            color = if (annotationColor == index) Color.Black else Color.Transparent,
                                            shape = CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(label, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (annotationText.isNotBlank()) {
                        val colorValue = highlightColors[annotationColor].first
                        val colorInt = colorValue.value.toLong().toInt()
                        viewModel.addAnnotation(
                            bookId = bookId,
                            chapterIndex = currentChapter,
                            text = annotationText,
                            note = annotationNote.ifBlank { null },
                            color = colorInt
                        )
                        android.widget.Toast.makeText(context, "笔记已保存", android.widget.Toast.LENGTH_SHORT).show()
                    }
                    showAnnotationDialog = false
                }) { Text("保存") }
            },
            dismissButton = {
                TextButton(onClick = { showAnnotationDialog = false }) { Text("取消") }
            }
        )
    }

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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("行距: ${"%.1f".format(dialogLineSpacing)}x", style = MaterialTheme.typography.titleSmall)
                    Slider(
                        value = dialogLineSpacing,
                        onValueChange = { dialogLineSpacing = it },
                        valueRange = 1.0f..2.5f,
                        steps = 14
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("主题", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(readerThemes) { _, rTheme ->
                            ThemeChip(
                                theme = rTheme,
                                isSelected = rTheme.name == dialogTheme,
                                onClick = { dialogTheme = rTheme.name }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setFontSize(dialogFontSize)
                    viewModel.setLineSpacing(dialogLineSpacing)
                    viewModel.setTheme(dialogTheme)
                    showSettingsDialog = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) { Text("取消") }
            }
        )
    }
}

@Composable
fun ThemeChip(
    theme: ReaderTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(theme.backgroundColor)
                .border(
                    width = if (isSelected) 2.dp else 0.dp,
                    color = if (isSelected) theme.indicatorColor else Color.Transparent,
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("Aa", color = theme.textColor, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(theme.label, style = MaterialTheme.typography.labelSmall)
    }
}

/**
 * Build an AnnotatedString with highlighted match text.
 */
private fun buildHighlightedString(
    fullText: String,
    matchText: String,
    isActive: Boolean
): androidx.compose.ui.text.AnnotatedString {
    if (!isActive || matchText.isBlank()) {
        return androidx.compose.ui.text.AnnotatedString(fullText)
    }
    return buildAnnotatedString {
        val lower = fullText.lowercase()
        val q = matchText.lowercase()
        var start = 0
        while (true) {
            val idx = lower.indexOf(q, start)
            if (idx < 0) {
                append(fullText.substring(start))
                break
            }
            append(fullText.substring(start, idx))
            withStyle(SpanStyle(background = HighlightColor, color = HighlightTextColor)) {
                append(fullText.substring(idx, idx + q.length))
            }
            start = idx + q.length
        }
    }
}

@Composable
private fun ChapterContent(
    content: String,
    fontSize: Float = 16f,
    lineSpacing: Float = 1.5f,
    textColor: Color = Color(0xFF333333),
    searchQuery: String = "",
    isSearchActive: Boolean = false
) {
    val paragraphStyle = TextStyle.Default.copy(
        fontSize = fontSize.sp,
        lineHeight = (fontSize * lineSpacing).sp,
        textIndent = TextIndent(firstLine = (fontSize * 2f).sp),
        color = textColor
    )

    val paragraphs = content.split("\n\n", "\r\n\r\n").filter { it.isNotBlank() }
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        paragraphs.forEach { paragraph ->
            if (isSearchActive && searchQuery.isNotBlank()) {
                Text(
                    text = buildHighlightedString(paragraph, searchQuery, true),
                    style = paragraphStyle,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else {
                Text(
                    text = paragraph.trim(),
                    style = paragraphStyle,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}