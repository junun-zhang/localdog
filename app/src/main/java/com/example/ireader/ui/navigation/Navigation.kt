package com.example.ireader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ireader.ui.bookshelf.BookshelfScreen
import com.example.ireader.ui.profile.ProfileScreen
import com.example.ireader.ui.reader.ReaderScreen
import com.example.ireader.ui.store.StoreScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onAddBookClick: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Bookshelf.route
    ) {
        composable(Screen.Bookshelf.route) {
            BookshelfScreen(navController = navController, onAddBookClick = onAddBookClick)
        }
        composable(Screen.Store.route) {
            StoreScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Reader.route) {
            ReaderScreen()
        }
    }
}

sealed class Screen(val route: String) {
    object Bookshelf : Screen("bookshelf")
    object Store : Screen("store")
    object Profile : Screen("profile")
    object Reader : Screen("reader/{bookId}") {
        fun createRoute(bookId: String): String {
            return "reader/$bookId"
        }
    }
}