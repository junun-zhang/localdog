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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
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
    var showDeleteDialog by remember { mutableStateOf<Book?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBookClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(painterResource(id = R.drawable.ic_add_book), contentDescription = stringResource(R.string.add_book))
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
                    onBookClick = { book ->
                        // 跳转到阅读界面
                        navController.navigate(Screen.Reader.createRoute(book.id))
                    },
                    onBookLongClick = { book ->
                        showDeleteDialog = book
                    }
                )
            }
        }
    }

    showDeleteDialog?.let { book ->
        DeleteDialog(
            book = book,
            onDismiss = { showDeleteDialog = null },
            onConfirm = {
                viewModel.deleteBook(book)
                showDeleteDialog = null
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
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                    val containerColor = when (book.format?.lowercase()) {
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
                    androidx.compose.material3.Surface(
                        color = containerColor,
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
private fun DeleteDialog(
    book: Book,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
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