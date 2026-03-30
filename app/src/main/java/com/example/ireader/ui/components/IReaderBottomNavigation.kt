package com.example.ireader.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.ireader.R

@Composable
fun IReaderBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    BottomAppBar {
        NavigationBarItem(
            selected = currentRoute == "bookshelf",
            onClick = { onNavigate("bookshelf") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_book_placeholder),
                    contentDescription = stringResource(R.string.bookshelf_title)
                )
            },
            label = {
                Text(text = stringResource(R.string.bookshelf_title))
            }
        )
        
        NavigationBarItem(
            selected = currentRoute == "bookstore",
            onClick = { onNavigate("bookstore") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookstore),
                    contentDescription = stringResource(R.string.bookstore_title)
                )
            },
            label = {
                Text(text = stringResource(R.string.bookstore_title))
            }
        )
        
        NavigationBarItem(
            selected = currentRoute == "reader",
            onClick = { onNavigate("reader") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_reader),
                    contentDescription = stringResource(R.string.reader_title)
                )
            },
            label = {
                Text(text = stringResource(R.string.reader_title))
            }
        )
        
        NavigationBarItem(
            selected = currentRoute == "bookmarks",
            onClick = { onNavigate("bookmarks") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = stringResource(R.string.bookmarks)
                )
            },
            label = {
                Text(text = stringResource(R.string.bookmarks))
            }
        )
        
        NavigationBarItem(
            selected = currentRoute == "notes",
            onClick = { onNavigate("notes") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_note),
                    contentDescription = stringResource(R.string.notes)
                )
            },
            label = {
                Text(text = stringResource(R.string.notes))
            }
        )
    }
}