package com.example.ireader.ui.reader

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    bookId: String,
    navController: NavController? = null,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val currentChapter by viewModel.currentChapter.collectAsStateWithLifecycle()
    val showMenu by viewModel.showMenu.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var showBookmarkDialog by remember { mutableStateOf(false) }
    var fontSize by remember { mutableStateOf(16f) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Check if current chapter is bookmarked
    val isBookmarked = viewModel.isChapterBookmarked(currentChapter)

    Scaffold(
        topBar = {
            if (showMenu) {
                TopAppBar(
                    title = { Text(book?.title ?: "阅读") },
                    navigationIcon = {
                        navController?.let { nc ->
                            IconButton(onClick = { nc.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
                                contentDescription = if (isBookmarked) "移除书签" else "添加书签"
                            )
                        }
                        IconButton(onClick = {
                            val chaptersJson = chapters.joinToString(",", prefix = "\"", postfix = "\"") { it }
                            navController?.navigate(Screen.Bookmarks.createRoute(bookId, chaptersJson))
                        }) {
                            Icon(imageVector = Icons.Default.Bookmark, contentDescription = "书签列表")
                        }
                        IconButton(onClick = { showSettingsDialog = true }) {
                            Icon(Icons.Default.Settings, contentDescription = "设置")
                        }
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                        itemsIndexed(listOf(chapters[currentChapter])) { index, content ->
                            ChapterContent(content = content, fontSize = fontSize)
                        }
                    }
                    Text(
                        text = "${currentChapter + 1}/${chapters.size}",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无内容")
                }
            }
        }
    }

    // Bookmark success snackbar
    if (showBookmarkDialog) {
        LaunchedEffect(Unit) {
            // Could show snackbar here
            showBookmarkDialog = false
        }
    }

    // Settings dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("阅读设置") },
            text = {
                Column {
                    Text("字体大小: ${fontSize.toInt()}sp")
                    Slider(
                        value = fontSize,
                        onValueChange = { fontSize = it },
                        valueRange = 12f..28f,
                        steps = 15
                    )
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

@Composable
private fun ChapterContent(content: String, fontSize: Float = 16f) {
    val paragraphStyle = TextStyle.Default.copy(
        fontSize = fontSize.sp,
        lineHeight = (fontSize * 1.5f).sp,
        textIndent = TextIndent(firstLine = (fontSize * 2f).sp),
        color = MaterialTheme.colorScheme.onBackground
    )

    val paragraphs = content.split("\n\n", "\r\n\r\n").filter { it.isNotBlank() }
    Column {
        paragraphs.forEach { paragraph ->
            Text(
                text = paragraph.trim(),
                style = paragraphStyle,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}
