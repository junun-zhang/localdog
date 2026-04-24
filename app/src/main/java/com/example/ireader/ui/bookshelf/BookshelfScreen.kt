package com.example.ireader.ui.bookshelf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ireader.R
import com.example.ireader.data.local.entity.Book
import com.example.ireader.ui.navigation.Screen

@Composable
fun BookshelfScreen(
    navController: NavController,
    onAddBookClick: () -> Unit,
    viewModel: BookshelfViewModel = hiltViewModel()
) {
    val books by viewModel.books.collectAsStateWithLifecycle()
    val selectedBooks by viewModel.selectedBooks.collectAsStateWithLifecycle()
    val isInMultiSelectMode by viewModel.isInMultiSelectMode.collectAsStateWithLifecycle()
    var showSingleDeleteDialog by remember { mutableStateOf<Book?>(null) }

    Scaffold(
        floatingActionButton = {
            if (!isInMultiSelectMode) {
                FloatingActionButton(
                    onClick = onAddBookClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(painterResource(id = R.drawable.ic_add_book), contentDescription = stringResource(R.string.add_book))
                }
            }
        },
        bottomBar = {
            if (isInMultiSelectMode) {
                BottomAppBar(
                    actions = {
                        TextButton(
                            onClick = { viewModel.clearSelection() },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text("取消")
                        }
                        Text(
                            text = "已选中 ${selectedBooks.size} 本",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { viewModel.deleteSelectedBooks() },
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "删除选中",
                                tint = MaterialTheme.colorScheme.onError
                            )
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
        ) {
            if (books.isEmpty()) {
                EmptyView(Modifier.align(Alignment.Center))
            } else {
                BooksGrid(
                    books = books,
                    selectedIds = selectedBooks,
                    isMultiSelectMode = isInMultiSelectMode,
                    onBookClick = { book ->
                        if (isInMultiSelectMode) {
                            viewModel.toggleSelection(book)
                        } else {
                            // 跳转到阅读界面
                            navController.navigate(Screen.Reader.createRoute(book.id))
                        }
                    },
                    onBookLongClick = { book ->
                        if (!isInMultiSelectMode) {
                            // Long click when not in multi-select mode enters multi-select
                            viewModel.toggleSelection(book)
                        }
                    }
                )
            }
        }
    }

    showSingleDeleteDialog?.let { book ->
        SingleDeleteDialog(
            book = book,
            onDismiss = { showSingleDeleteDialog = null },
            onConfirm = {
                viewModel.deleteBook(book)
                showSingleDeleteDialog = null
            }
        )
    }
}

@Composable
private fun EmptyView(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.empty_bookshelf))
    }
}

@Composable
private fun BooksGrid(
    books: List<Book>,
    selectedIds: Set<String>,
    isMultiSelectMode: Boolean,
    onBookClick: (Book) -> Unit,
    onBookLongClick: (Book) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                isSelected = selectedIds.contains(book.id),
                onClick = { onBookClick(book) },
                onLongClick = { onBookLongClick(book) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookCard(
    book: Book,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    } else {
        CardDefaults.cardColors().containerColor
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column {
            // 封面区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (!book.coverPath.isNullOrEmpty()) {
                    AsyncImage(
                        model = book.coverPath,
                        contentDescription = book.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // 根据格式选择不同的默认封面图标
                    val defaultIcon = when (book.format?.lowercase()) {
                        "txt" -> R.drawable.ic_txt_cover
                        "pdf" -> R.drawable.ic_pdf_cover
                        "epub" -> R.drawable.ic_epub_cover
                        else -> R.drawable.ic_default_book_cover
                    }
                    // 根据格式选择不同的容器颜色以便区分
                    val coverContainerColor = when (book.format?.lowercase()) {
                        "txt" -> MaterialTheme.colorScheme.primaryContainer
                        "pdf" -> MaterialTheme.colorScheme.secondaryContainer
                        "epub" -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.primaryContainer
                    }
                    val contentColor = when (book.format?.lowercase()) {
                        "txt" -> MaterialTheme.colorScheme.onPrimaryContainer
                        "pdf" -> MaterialTheme.colorScheme.onSecondaryContainer
                        "epub" -> MaterialTheme.colorScheme.onTertiaryContainer
                        else -> MaterialTheme.colorScheme.onPrimaryContainer
                    }
                    // 默认封面
                    Surface(
                        color = coverContainerColor,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = defaultIcon),
                            contentDescription = book.title,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize(0.6f),
                            tint = contentColor
                        )
                    }
                }

                // 选中标记 - 半透明覆盖层
                if (isSelected) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Checkmark could be added here
                        }
                    }
                }
            }

            // 书名作者
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!book.author.isNullOrEmpty()) {
                    Text(
                        text = book.author,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // 进度条
                if (book.progress > 0f) {
                    androidx.compose.material3.LinearProgressIndicator(
                        progress = { book.progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(1.5.dp)),
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleDeleteDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.book_delete)) },
        text = { Text(stringResource(R.string.book_delete_confirm)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}