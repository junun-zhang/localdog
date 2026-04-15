package com.example.ireader.ui.bookshelf

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ireader.R
import com.example.ireader.data.model.Book
import com.example.ireader.ui.util.*

@Composable
fun BookItem(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth() // 让卡片填满其容器的宽度
            .aspectRatio(3f / 4f) // 保持宽高比
            .clip(RoundedCornerShape(16.dp * getFontSizeMultiplier()))
            .clickable { onClick() }
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp * getFontSizeMultiplier())),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp,
            hoveredElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp * getFontSizeMultiplier()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp * getFontSizeMultiplier())
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp * getFontSizeMultiplier())
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = book.title.take(1),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = MaterialTheme.typography.displaySmall.fontSize * getFontSizeMultiplier() * 0.8f,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = MaterialTheme.typography.titleSmall.fontSize * getFontSizeMultiplier(),
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = book.author,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize * getFontSizeMultiplier() * 0.9f
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BookListItem(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp * getFontSizeMultiplier()),
        shape = RoundedCornerShape(12.dp * getFontSizeMultiplier()),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp * getFontSizeMultiplier()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book cover thumbnail
            Box(
                modifier = Modifier
                    .size(64.dp * getFontSizeMultiplier())
                    .clip(RoundedCornerShape(8.dp * getFontSizeMultiplier()))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_book_placeholder),
                    contentDescription = "Book cover",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp * getFontSizeMultiplier()))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * getFontSizeMultiplier()
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp * getFontSizeMultiplier()))
                
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize * getFontSizeMultiplier()
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(4.dp * getFontSizeMultiplier()))
                
                Text(
                    text = book.format + " • " + book.getFileSizeString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize * getFontSizeMultiplier()
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}