package com.example.ireader.ui.store

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ireader.data.remote.StoreBook

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    navController: androidx.navigation.NavController,
    viewModel: StoreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (showSearch) "" else "图书商城") },
                actions = {
                    if (!showSearch) {
                        IconButton(onClick = { showSearch = true }) {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        }
                    } else {
                        TextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                viewModel.search(it)
                            },
                            placeholder = { Text("搜索书名或作者...") },
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    showSearch = false
                                    searchText = ""
                                    viewModel.search("")
                                }) {
                                    Icon(Icons.Default.Close, "关闭")
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (state.loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.error != null && state.books.isEmpty() && state.featured.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("加载失败", style = MaterialTheme.typography.titleMedium)
                    Text(state.error ?: "", style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadInitial() }) { Text("重试") }
                }
            }
        } else {
            Column(Modifier.fillMaxSize().padding(padding)) {
                // Category filter chips
                if (state.categories.isNotEmpty()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = state.selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("全部") }
                        )
                        state.categories.forEach { cat ->
                            FilterChip(
                                selected = state.selectedCategory == cat,
                                onClick = { viewModel.selectCategory(cat) },
                                label = { Text(cat) }
                            )
                        }
                    }
                }

                // Featured section (only at top when no filter/search)
                if (state.selectedCategory == null && state.searchQuery.isBlank()
                    && state.featured.isNotEmpty()) {
                    Text("推荐书籍",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp))
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.featured, key = { it.id }) { book ->
                            FeaturedBookCard(book, modifier = Modifier.width(180.dp))
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("全部书籍",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp))
                    Spacer(Modifier.height(4.dp))
                }

                // Book grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.books, key = { it.id }) { book ->
                        StoreBookCard(book)
                    }

                    if (state.loadingMore) {
                        item {
                            Box(Modifier.fillMaxWidth().padding(16.dp),
                                contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedBookCard(book: StoreBook, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.clickable { /* detail in future */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                Modifier.fillMaxWidth().height(140.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(book.format.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
            Column(Modifier.padding(8.dp)) {
                Text(book.title, style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold)
                Text(book.author, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★ %.1f".format(book.rating),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFFB300))
                    if (book.isFree) {
                        Spacer(Modifier.width(4.dp))
                        Text("免费", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreBookCard(book: StoreBook) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column {
            Box(
                Modifier.fillMaxWidth().height(160.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(book.format.uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(Modifier.padding(8.dp)) {
                Text(book.title, style = MaterialTheme.typography.titleSmall,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold)
                Text(book.author, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("★ %.1f".format(book.rating),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFFB300))
                    Spacer(Modifier.width(8.dp))
                    Text("${book.downloadCount}次下载",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { /* download in future */ },
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(if (book.isFree) "免费下载" else "购买",
                        style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
