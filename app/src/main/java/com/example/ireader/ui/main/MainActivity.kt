package com.example.ireader.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ireader.ui.theme.IReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderApp()
        }
    }
}

@Composable
fun IReaderApp() {
    val navController = rememberNavController()
    
    IReaderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = "bookshelf"
            ) {
                composable("bookshelf") {
                    BookShelfScreen(navController = navController)
                }
                composable("reader/{bookId}") { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                    ReaderScreen(bookId = bookId, navController = navController)
                }
                composable("settings") {
                    SettingsScreen(navController = navController)
                }
            }
        }
    }
}

@Composable
fun BookShelfScreen(navController: NavHostController) {
    Text(text = "书架 - 显示本地书籍列表")
}

@Composable
fun ReaderScreen(bookId: String, navController: NavHostController) {
    Text(text = "阅读器 - 书籍ID: $bookId")
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Text(text = "设置 - 阅读偏好设置")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IReaderTheme {
        IReaderApp()
    }
}