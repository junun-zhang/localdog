package com.example.ireader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ireader.ui.bookmark.BookmarkScreen
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
        composable(
            route = Screen.Reader.route,
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            ReaderScreen(bookId = bookId)
        }
        composable(
            route = Screen.Bookmarks.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType },
                navArgument("chapters") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            val chaptersJson = backStackEntry.arguments?.getString("chapters") ?: "[]"
            // Simple JSON array parsing: ["Chapter1","Chapter2"]
            val chapterNames = chaptersJson
                .removePrefix("[")
                .removeSuffix("]")
                .split(",")
                .map { it.trim().removeSurrounding("\"") }
                .filter { it.isNotEmpty() }
            BookmarkScreen(
                bookId = bookId,
                chapterNames = chapterNames,
                onBookmarkClick = { chapterIndex ->
                    // Navigate back to reader at specific chapter
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
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
    object Bookmarks : Screen("bookmarks/{bookId}/{chapters}") {
        fun createRoute(bookId: String, chaptersJson: String): String {
            return "bookmarks/$bookId/$chaptersJson"
        }
    }
}
