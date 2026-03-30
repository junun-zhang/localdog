package com.example.ireader.ui.reader

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavHostController
import com.example.ireader.R
import com.example.ireader.data.model.Book
import com.example.ireader.ui.components.LoadingState

@Composable
fun ReaderScreen(
    bookId: String,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel: ReaderViewModel = hiltViewModel()
    val context = LocalContext.current
    
    // 启动文件选择器（用于PDF/TXT）
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let { 
                viewModel.loadBookFromUri(it, context)
            }
        }
    )
    
    // 加载书籍
    LifecycleEventEffect(Lifecycle.Event.ON_CREATE) {
        viewModel.loadBook(bookId)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = uiState.book?.title ?: stringResource(R.string.reader_title),
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
                        Button(onClick = { viewModel.loadBook(bookId) }) {
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
                    when (uiState.book?.format?.lowercase()) {
                        "epub" -> {
                            EPUBReader(
                                filePath = uiState.book.filePath,
                                onProgressUpdate = { progress ->
                                    viewModel.updateReadingProgress(progress)
                                }
                            )
                        }
                        "pdf" -> {
                            PDFReader(
                                filePath = uiState.book.filePath,
                                onPageChanged = { page ->
                                    viewModel.updateReadingPage(page)
                                }
                            )
                        }
                        "txt" -> {
                            TXTReader(
                                filePath = uiState.book.filePath,
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
                                    text = "不支持的文件格式: ${uiState.book?.format}",
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