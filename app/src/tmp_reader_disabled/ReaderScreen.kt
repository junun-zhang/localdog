package com.example.ireader.ui.reader

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.example.ireader.R
import com.example.ireader.data.model.Book
import com.example.ireader.ui.components.LoadingState

@Composable
fun ReaderScreen(
    book: Book,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: ReaderViewModel = hiltViewModel()
    val context = LocalContext.current
    
    // 加载书籍
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.loadBook(book)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = book.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        // 保存阅读进度
                        viewModel.saveReadingProgress()
                        navController.popBackStack() 
                    }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        viewModel.showSettings = !viewModel.showSettings
                    }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.loadingState == LoadingState.Success) {
                ExtendedFloatingActionButton(
                    onClick = { 
                        // 添加书签
                        viewModel.addBookmark(book.id, getCurrentPosition(book))
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = "Add Bookmark"
                        )
                    },
                    text = {
                        Text("书签")
                    }
                )
            }
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
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "无法加载书籍内容",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBook(book) }) {
                            Text("重试")
                        }
                    }
                }
            }
            LoadingState.Success -> {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // 根据文件格式显示不同的阅读器
                    when (book.format.lowercase()) {
                        "epub" -> {
                            EPUBReader(
                                filePath = book.filePath,
                                onProgressUpdate = { progress ->
                                    viewModel.updateReadingProgress(progress)
                                }
                            )
                        }
                        "pdf" -> {
                            PDFReader(
                                filePath = book.filePath,
                                onPageChanged = { page ->
                                    viewModel.updateReadingPage(page)
                                }
                            )
                        }
                        "txt" -> {
                            TXTReader(
                                filePath = book.filePath,
                                onScroll = { position ->
                                    viewModel.updateReadingPosition(position)
                                }
                            )
                        }
                        else -> {
                            // 不支持的格式
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "不支持的文件格式: ${book.format}",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    
                    // 阅读设置面板
                    if (viewModel.showSettings) {
                        ReadingSettings(
                            settings = viewModel.readingSettings,
                            onSettingsChanged = { newSettings ->
                                viewModel.updateReadingSettings(newSettings)
                            },
                            onClose = { viewModel.showSettings = false }
                        )
                    }
                }
            }
        }
    }
}

// 获取当前阅读位置的辅助函数
private fun getCurrentPosition(book: Book): String {
    return when (book.format.lowercase()) {
        "pdf" -> "page_${book.lastReadPage}"
        "txt" -> "line_${book.lastReadPage}"
        else -> "chapter_1"
    }
}