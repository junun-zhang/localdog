package com.example.ireader.ui.bookstore

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ireader.R
import com.example.ireader.data.model.BookStoreItem

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
            // 封面图片（使用 Coil 加载网络图片）
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = "${book.title} cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = androidx.compose.material3.Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Book,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
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