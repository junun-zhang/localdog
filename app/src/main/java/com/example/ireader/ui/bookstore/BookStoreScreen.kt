package com.example.ireader.ui.bookstore

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ireader.R
import com.example.ireader.data.model.BookStoreItem
import com.example.ireader.ui.components.LoadingState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookStoreScreen(
    onBookClick: (BookStoreItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: BookStoreViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bookstore_title)) },
                actions = {
                    IconButton(onClick = { viewModel.refreshBooks() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (uiState.loadingState) {
            LoadingState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            LoadingState.Error -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.CloudOff,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.bookstore_error),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refreshBooks() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }
            LoadingState.Success -> {
                if (uiState.books.isEmpty()) {
                    Box(
                        modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.bookstore_empty))
                    }
                } else {
                    // 使用 Pager 显示不同分类
                    val pagerState = rememberPagerState { uiState.categories.size }
                    
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        // 分类标签
                        TabRow(
                            selectedTabIndex = pagerState.currentPage,
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            uiState.categories.forEachIndexed { index, category ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = { 
                                        // 注意：这里简化处理，实际应该有更复杂的逻辑
                                        viewModel.loadBooksByCategory(category)
                                    },
                                    text = { Text(category) }
                                )
                            }
                        }
                        
                        // 图书网格
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.weight(1f)
                        ) { page ->
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 150.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(uiState.books.filter { it.category == uiState.categories[page] }) { book ->
                                    BookStoreItem(
                                        book = book,
                                        onItemClick = onBookClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookStoreItem(
    book: BookStoreItem,
    onItemClick: (BookStoreItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .clickable { onItemClick(book) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            // 封面图片（使用占位符）
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                // 这里可以使用 Coil 或 Glide 加载网络图片
                androidx.compose.material3.Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 书名
            Text(
                text = book.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = Alignment.Center
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 作者
            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // 价格或免费标识
            if (book.isFree) {
                Text(
                    text = stringResource(R.string.free),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                Text(
                    text = "${book.price}¥",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}