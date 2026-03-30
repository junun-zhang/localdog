package com.example.ireader.ui.bookstore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.ireader.R
import com.example.ireader.data.model.BookStoreItem
import com.example.ireader.ui.components.LoadingState

@Composable
fun BookDetailScreen(
    bookId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: BookStoreViewModel = hiltViewModel()
    
    // 加载书籍详情
    LaunchedEffect(bookId) {
        viewModel.loadBookDetail(bookId)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text("书籍详情")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "返回"
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
                            imageVector = androidx.compose.material.icons.Icons.Default.Error,
                            contentDescription = "错误",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "无法加载书籍详情",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBookDetail(bookId) }) {
                            Text("重试")
                        }
                    }
                }
            }
            LoadingState.Success -> {
                uiState.bookDetail?.let { book ->
                    BookDetailContent(book = book, onDownloadClick = { 
                        viewModel.downloadBook(book)
                    })
                }
            }
        }
    }
}

@Composable
fun BookDetailContent(
    book: BookStoreItem,
    onDownloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 书籍封面
        AsyncImage(
            model = book.coverUrl,
            contentDescription = "书籍封面",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 书籍标题
        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 作者信息
        Text(
            text = "作者: ${book.author}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 分类信息
        Text(
            text = "分类: ${book.category}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 评分
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Star,
                contentDescription = "评分",
                tint = Color.Yellow
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${book.rating} (${book.reviewCount}条评论)",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 价格信息
        if (book.price > 0) {
            Text(
                text = "价格: ¥${book.price}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = "免费",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 简介
        Text(
            text = "简介",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = book.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 下载按钮
        Button(
            onClick = onDownloadClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !book.isDownloaded
        ) {
            if (book.isDownloaded) {
                Text("已下载")
            } else {
                Text("下载")
            }
        }
    }
}