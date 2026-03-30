package com.example.ireader.ui.components

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.ireader.R

enum class BottomNavigationItem(
    @StringRes val title: Int,
    val icon: Int,
    val route: String
) {
    Bookshelf(R.string.bookshelf_title, R.drawable.ic_book_placeholder, "bookshelf"),
    Store(R.string.bookstore_title, R.drawable.ic_store, "store"),
    Reader(R.string.reader_title, R.drawable.ic_reader, "reader"),
    Bookmarks(R.string.bookmarks, R.drawable.ic_bookmark, "bookmarks"),
    Notes(R.string.notes, R.drawable.ic_note, "notes"),
    Settings(R.string.settings_title, android.R.drawable.ic_menu_preferences, "settings")
}