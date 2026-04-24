package com.example.ireader.ui.reader

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val book by viewModel.book.collectAsStateWithLifecycle()
    val chapters by viewModel.chapters.collectAsStateWithLifecycle()
    val currentChapter by viewModel.currentChapter.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book?.title ?: "阅读") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            val screenWidth = this.size.width
                            if (offset.x < screenWidth / 3) {
                                if (viewModel.prevChapter()) {
                                    scope.launch {
                                        listState.scrollToItem(0)
                                    }
                                }
                            } else if (offset.x > screenWidth * 2 / 3) {
                                if (viewModel.nextChapter()) {
                                    scope.launch {
                                        listState.scrollToItem(0)
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            if (chapters.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        state = listState
                    ) {
                        itemsIndexed(listOf(chapters[currentChapter])) { index, content ->
                            ChapterContent(content = content)
                        }
                    }

                    // 页码指示器
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
}

@Composable
private fun ChapterContent(content: String) {
    val paragraphStyle = TextStyle.Default.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        textIndent = TextIndent(firstLine = 32.sp),
        color = MaterialTheme.colorScheme.onBackground
    )

    val paragraphs = content.split("\n\n", "\r\n\r\n").filter { it.isNotBlank() }
    LazyColumn {
        itemsIndexed(paragraphs) { _, paragraph ->
            Text(
                text = paragraph.trim(),
                style = paragraphStyle,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}