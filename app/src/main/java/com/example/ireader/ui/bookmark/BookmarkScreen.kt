package com.example.ireader.ui.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ireader.data.local.entity.Annotation
import com.example.ireader.data.local.entity.Bookmark
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    bookId: String,
    onNavigateBack: () -> Unit,
    onAnnotationClick: ((chapterIndex: Int, highlightedText: String) -> Unit)? = null
) {
    val viewModel: BookmarkViewModel = hiltViewModel()
    val bookmarks by viewModel.getBookmarks(bookId).collectAsState(initial = emptyList())
    val annotations by viewModel.getAnnotations(bookId).collectAsState(initial = emptyList())
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("书签", "笔记")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("书签与笔记") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                        icon = {
                            Icon(
                                imageVector = if (index == 0) Icons.Default.Bookmark else Icons.AutoMirrored.Filled.Note,
                                contentDescription = title
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> BookmarkList(
                    bookmarks = bookmarks,
                    onBookmarkClick = { /* could navigate to chapter */ },
                    onDeleteBookmark = { bookmark ->
                        viewModel.deleteBookmark(bookmark)
                    }
                )
                1 -> AnnotationList(
                    annotations = annotations,
                    onAnnotationClick = onAnnotationClick,
                    onDeleteAnnotation = { annotation ->
                        viewModel.deleteAnnotation(annotation)
                    }
                )
            }
        }
    }
}

@Composable
fun BookmarkList(
    bookmarks: List<Bookmark>,
    onBookmarkClick: (Bookmark) -> Unit,
    onDeleteBookmark: (Bookmark) -> Unit
) {
    if (bookmarks.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无书签", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(bookmarks, key = { it.id }) { bookmark ->
                BookmarkItem(
                    bookmark = bookmark,
                    onClick = { onBookmarkClick(bookmark) },
                    onDelete = { onDeleteBookmark(bookmark) }
                )
            }
        }
    }
}

@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val timeStr = dateFormat.format(Date(bookmark.createTime))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "第${bookmark.chapterIndex + 1}章", style = MaterialTheme.typography.titleMedium)
                Text(text = timeStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "删除书签")
            }
        }
    }
}

@Composable
fun AnnotationList(
    annotations: List<Annotation>,
    onAnnotationClick: ((chapterIndex: Int, highlightedText: String) -> Unit)? = null,
    onDeleteAnnotation: (Annotation) -> Unit
) {
    if (annotations.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无笔记", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(annotations, key = { it.id }) { annotation ->
                AnnotationItem(
                    annotation = annotation,
                    onClick = {
                        if (onAnnotationClick != null) {
                            onAnnotationClick(annotation.chapterIndex, annotation.highlightedText ?: "")
                        }
                    },
                    onDelete = { onDeleteAnnotation(annotation) }
                )
            }
        }
    }
}

@Composable
fun AnnotationItem(
    annotation: Annotation,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val timeStr = dateFormat.format(Date(annotation.createTime))
    
    // Convert Int color to Compose Color
    val highlightColor = Color(annotation.color)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Colored indicator bar
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(highlightColor.copy(alpha = 0.8f))
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "第${annotation.chapterIndex + 1}章", style = MaterialTheme.typography.titleSmall)
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "删除笔记")
                    }
                }
                annotation.highlightedText?.let { text ->
                    Text(
                        text = "\"$text\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = highlightColor,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                annotation.note?.let { note ->
                    Text(
                        text = "批注: $note",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Text(text = timeStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
