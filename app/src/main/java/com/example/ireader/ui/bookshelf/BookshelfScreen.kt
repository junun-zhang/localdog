package com.example.ireader.ui.bookshelf

import android.net.Uri
import kotlinx.coroutines.launch
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.ireader.data.local.entity.Book
import com.example.ireader.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookshelfScreen(
    navController: NavController,
    onAddBookClick: () -> Unit,
    viewModel: BookshelfViewModel = hiltViewModel()
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val selectedBooks by viewModel.selectedBooks.collectAsStateWithLifecycle()
    val isInMultiSelectMode by viewModel.isInMultiSelectMode.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    var showSearch by remember { mutableStateOf(false) }

    // Handle back button for search view
    BackHandler(enabled = showSearch) {
        showSearch = false
        viewModel.updateSearchQuery("")
    }

    // Handle back button for multi-select mode
    BackHandler(enabled = selectedBooks.isNotEmpty()) {
        viewModel.clearSelection()
    }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            if (isInMultiSelectMode) {
                TopAppBar(
                    title = { Text("${selectedBooks.size} \u5df2\u9009\u62e9") },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.clearSelection() }) {
                            Text("<", style = MaterialTheme.typography.titleLarge)
                        }
                    },
                    actions = {
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "\u5220\u9664")
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = { Text(if (showSearch) "" else "\u6211\u7684\u4e66\u67b6") },
                    actions = {
                        if (!showSearch) {
                            IconButton(onClick = { showSearch = true }) {
                                Icon(Icons.Default.Search, contentDescription = "\u641c\u7d22")
                            }
                            // FIX: Directly call onAddBookClick() instead of launching file picker
                            // The old code used rememberLauncherForActivityResult which ignored
                            // the selected URIs and called onAddBookClick() anyway.
                            IconButton(onClick = { onAddBookClick() }) {
                                Icon(Icons.Default.Add, contentDescription = "\u5bfc\u5165\u4e66\u7c4d")
                            }
                        } else {
                            TextField(
                                value = searchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                placeholder = { Text("\u641c\u7d22\u4e66\u540d...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        showSearch = false
                                        viewModel.updateSearchQuery("")
                                    }) {
                                        Text("X")
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        // Category filter chips
        val categoryScrollState = rememberScrollState()
        Column {
            if (categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(categoryScrollState)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectCategory(null) },
                        label = { Text("全部") }
                    )
                    categories.forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { viewModel.selectCategory(cat) },
                            label = { Text(cat) }
                        )
                    }
                }
            }
            if (books.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\u4e66\u67b6\u662f\u7a7a\u7684", style = MaterialTheme.typography.titleMedium)
                    Text("\u70b9\u51fb\u53f3\u4e0a\u89d2 + \u5bfc\u5165\u4e66\u7c4d", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(books, key = { it.id }) { book ->
                    BookCard(
                        book = book,
                        isSelected = selectedBooks.contains(book.id),
                        onClick = { navController.navigate(Screen.Reader.createRoute(book.id)) },
                        onLongClick = { viewModel.toggleSelection(book) }
                    )
                }
            }
        }
    }

    val predefinedCategories = listOf(
        "小说", "科技", "历史", "哲学", "文学",
        "经济", "教育", "艺术", "生活", "其他"
    )

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
            title = { Text("设置分类") },
            text = {
                Column {
                    Text("为 ${selectedBooks.size} 本书选择分类：")
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(predefinedCategories) { cat ->
                            FilterChip(
                                selected = cat == editingCategory,
                                onClick = { editingCategory = cat },
                                label = { Text(cat, style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                        item {
                            FilterChip(
                                selected = editingCategory == "清除",
                                onClick = { editingCategory = "清除" },
                                label = { Text("清除", style = MaterialTheme.typography.bodySmall) }
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val cat = if (editingCategory == "清除") null else editingCategory
                    scope.launch {
                        selectedBooks.forEach { bookId ->
                            viewModel.updateBookCategory(bookId, cat)
                        }
                    }
                    showCategoryDialog = false
                    editingCategory = ""
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showCategoryDialog = false
                    editingCategory = ""
                }) { Text("取消") }
            }
        )
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("\u786e\u8ba4\u5220\u9664") },
            text = { Text("\u786e\u5b9a\u8981\u5220\u9664\u9009\u4e2d\u7684 ${selectedBooks.size} \u672c\u4e66\u5417\uff1f\u8fd9\u5c06\u4ece\u4e66\u67b6\u548c\u8bbe\u5907\u4e2d\u540c\u65f6\u5220\u9664\u3002") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteSelectedBooks()
                    showDeleteConfirm = false
                }) {
                    Text("\u5220\u9664", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("\u53d6\u6d88")
                }
            }
        )
    }
}

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookCard(
    book: Book,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = book.format?.uppercase() ?: "TXT",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (!book.category.isNullOrBlank()) {
                Text(
                    text = book.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            Text(
                text = book.title ?: "\u672a\u77e5\u4e66\u540d",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (book.progress > 0) {
                Text(
                    text = "\u9605\u8bfb\u8fdb\u5ea6 ${(book.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
