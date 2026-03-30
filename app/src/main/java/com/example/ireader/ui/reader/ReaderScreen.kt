package com.example.ireader.ui.reader

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.ireader.R
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import kotlinx.coroutines.launch

@Composable
fun ReaderScreen(
    bookId: String,
    navController: NavHostController,
    bookRepository: BookRepository,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    var book by remember { mutableStateOf<Book?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    
    // Load book data
    LaunchedEffect(bookId) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            try {
                val loadedBook = bookRepository.getBookById(bookId)
                if (loadedBook != null) {
                    book = loadedBook
                } else {
                    error = "书籍未找到"
                }
            } catch (e: Exception) {
                error = "加载书籍失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = book?.title ?: stringResource(R.string.reader_title),
                        maxLines = 1
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 阅读设置 */ }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Settings,
                            contentDescription = "阅读设置"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("加载中...")
                    }
                }
            } else if (error != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
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
                        Text(error!!, color = MaterialTheme.colorScheme.error)
                    }
                }
            } else if (book != null) {
                val currentBook = book!!
                when (currentBook.format.lowercase()) {
                    "epub" -> {
                        EPUBReader(
                            context = context,
                            filePath = currentBook.filePath,
                            onProgressUpdate = { progress ->
                                // TODO: 更新阅读进度
                                // bookRepository.updateBookProgress(currentBook.id, progress)
                            }
                        )
                    }
                    "pdf" -> {
                        PDFReader(
                            context = context,
                            filePath = currentBook.filePath,
                            onPageChanged = { page ->
                                // TODO: 更新阅读进度
                                // bookRepository.updateBookPage(currentBook.id, page)
                            }
                        )
                    }
                    "txt" -> {
                        TXTReader(
                            context = context,
                            filePath = currentBook.filePath,
                            onScroll = { position ->
                                // TODO: 更新阅读位置
                            }
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("不支持的文件格式: ${currentBook.format}")
                        }
                    }
                }
            }
        }
    }
}