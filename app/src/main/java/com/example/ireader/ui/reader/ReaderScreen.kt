package com.example.ireader.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ireader.ui.navigation.Screen
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

// Highlight colors for annotations
val highlightColors = listOf(
    Color(0xFFFFF176) to "黄色",
    Color(0xFF81C784) to "绿色",
    Color(0xFF64B5F6) to "蓝色",
    Color(0xFFFF8A65) to "橙色"
)

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

    // Route to different readers based on format
    when {
        isPdf -> {
            PdfReaderScreen(
                filePath = book?.filePath,
                title = book?.title ?: "PDF阅读",
                onNavigateBack = { navController?.popBackStack() }
            )
        }
        isEpub && epubInfo != null -> {
            EpubReaderScreen(
                filePath = book?.filePath,
                title = book?.title ?: "EPUB阅读",
                onNavigateBack = { navController?.popBackStack() }
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
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showBookmarkDialog by remember { mutableStateOf(false) }
    val fontSize by viewModel.fontSize.collectAsStateWithLifecycle()
    val lineSpacing by viewModel.lineSpacing.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    var showSettingsDialog by remember { mutableStateOf(false) }
    var dialogFontSize by remember { mutableStateOf(viewModel.fontSize.value) }
    var dialogLineSpacing by remember { mutableStateOf(viewModel.lineSpacing.value) }
    var dialogTheme by remember { mutableStateOf(viewModel.theme.value) }

    // Annotation dialog state
    var showAnnotationDialog by remember { mutableStateOf(false) }
    var annotationText by remember { mutableStateOf("") }
    var annotationNote by remember { mutableStateOf("") }
    var annotationColor by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    val currentTheme = readerThemes.find { it.name == theme } ?: readerThemes[0]

    val isBookmarked = viewModel.isChapterBookmarked(currentChapter)

    Scaffold(
        containerColor = currentTheme.backgroundColor,
        topBar = {
            if (showMenu) {
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
                .pointerInput(showMenu) {
                    detectTapGestures(
                        onTap = { offset ->
                            val screenWidth = this.size.width
                            if (offset.x < screenWidth / 3) {
                                if (!showMenu) {
                                    if (viewModel.prevChapter()) {
                                        scope.launch { listState.scrollToItem(0) }
                                    }
                                }
                            } else if (offset.x > screenWidth * 2 / 3) {
                                if (!showMenu) {
                                    if (viewModel.nextChapter()) {
                                        scope.launch { listState.scrollToItem(0) }
                                    }
                                }
                            } else {
                                viewModel.toggleMenu()
                            }
                        }
                    )
                }
        ) {
            if (chapters.isNotEmpty() && currentChapter < chapters.size) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        state = listState
                    ) {
                        itemsIndexed(listOf(chapters[currentChapter])) { _, content ->
                            ChapterContent(
                                content = content,
                                fontSize = fontSize,
                                lineSpacing = lineSpacing,
                                textColor = currentTheme.textColor
                            )
                        }
                    }
                    Text(
                        text = "${currentChapter + 1}/${chapters.size}",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = currentTheme.textColor
                    )
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

    if (showBookmarkDialog) {
        LaunchedEffect(Unit) {
            showBookmarkDialog = false
        }
    }

    // Annotation dialog
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
                }) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAnnotationDialog = false }) {
                    Text("取消")
                }
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
                        items(readerThemes) { rTheme ->
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

@Composable
private fun ChapterContent(
    content: String,
    fontSize: Float = 16f,
    lineSpacing: Float = 1.5f,
    textColor: Color = Color(0xFF333333)
) {
    val paragraphStyle = TextStyle.Default.copy(
        fontSize = fontSize.sp,
        lineHeight = (fontSize * lineSpacing).sp,
        textIndent = TextIndent(firstLine = (fontSize * 2f).sp),
        color = textColor
    )

    val paragraphs = content.split("\n\n", "\r\n\r\n").filter { it.isNotBlank() }
    Column {
        paragraphs.forEach { paragraph ->
            Text(
                text = paragraph.trim(),
                style = paragraphStyle,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
