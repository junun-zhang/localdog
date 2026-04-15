package com.example.ireader.ui.bookmarks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.background
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ireader.R
import com.example.ireader.data.model.Bookmark
import com.example.ireader.ui.main.BookmarksViewModel
import com.example.ireader.ui.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksScreen(
    navController: NavHostController,
    bookId: String? = null
) {
    val viewModel: BookmarksViewModel = viewModel()
    val bookmarks = if (bookId != null) {
        viewModel.getBookmarksForBook(bookId)
    } else {
        viewModel.allBookmarks
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.bookmarks), 
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize * getFontSizeMultiplier()
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val bookmarkList = bookmarks.value.orEmpty()
        
        if (bookmarkList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp * getFontSizeMultiplier())
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp * getFontSizeMultiplier()),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                    Text(
                        text = stringResource(R.string.no_bookmarks_found),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = MaterialTheme.typography.headlineSmall.fontSize * getFontSizeMultiplier()
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "在阅读时添加书签以便快速返回",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = MaterialTheme.typography.bodyMedium.fontSize * getFontSizeMultiplier()
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(getPadding()),
                verticalArrangement = Arrangement.spacedBy(getSpacing())
            ) {
                items(bookmarkList) { bookmark ->
                    BookmarkItem(
                        bookmark = bookmark,
                        onClick = {
                            // 跳转到书签位置
                            navController.navigate("reader/${bookmark.bookId}?page=${bookmark.page}")
                        },
                        onDelete = {
                            viewModel.deleteBookmark(bookmark.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp * getFontSizeMultiplier()))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp,
            pressedElevation = 1.dp,
            hoveredElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp * getFontSizeMultiplier()),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp * getFontSizeMultiplier())
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp * getFontSizeMultiplier())
                        .clip(RoundedCornerShape(12.dp * getFontSizeMultiplier()))
                        .background(MaterialTheme.colorScheme.primary)
                        
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp * getFontSizeMultiplier()),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = bookmark.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = MaterialTheme.typography.titleMedium.fontSize * getFontSizeMultiplier()
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp * getFontSizeMultiplier()))
                    
                    Text(
                        text = "第 ${bookmark.page} 页 • ${bookmark.getCreatedAtString()}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize * getFontSizeMultiplier() * 0.9f
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(36.dp * getFontSizeMultiplier())
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete bookmark",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { 
                Text(
                    "删除书签", 
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize * getFontSizeMultiplier()
                ) 
            },
            text = { 
                Text(
                    "确定要删除这个书签吗？此操作无法撤销。",
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize * getFontSizeMultiplier()
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        "删除", 
                        color = MaterialTheme.colorScheme.onError,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize * getFontSizeMultiplier()
                    )
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "取消",
                        fontSize = MaterialTheme.typography.labelLarge.fontSize * getFontSizeMultiplier()
                    )
                }
            }
        )
    }
}