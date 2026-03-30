package com.example.ireader.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ireader.data.model.Book
import com.example.ireader.ui.bookmarks.BookmarksScreen
import com.example.ireader.ui.bookshelf.BookShelfScreen
import com.example.ireader.ui.components.IReaderBottomNavigation
import com.example.ireader.ui.highlights.HighlightsScreen
import com.example.ireader.ui.notes.NotesScreen
import com.example.ireader.ui.reader.ReaderScreen
import com.example.ireader.ui.settings.SettingsScreen
import com.example.ireader.ui.theme.IReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val filePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let { 
            // 处理导入的文件
            val booksViewModel: BooksViewModel = viewModel()
            booksViewModel.addBookFromUri(this, uri)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderApp(filePicker = { filePicker.launch(arrayOf("application/epub+zip", "application/pdf", "text/plain")) })
        }
    }
}

@Composable
fun IReaderApp(filePicker: () -> Unit) {
    val navController = rememberNavController()
    val booksViewModel: BooksViewModel = viewModel()
    val books = booksViewModel.books
    
    IReaderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            IReaderScaffold(navController = navController, books = books, onImportClick = filePicker)
        }
    }
}

@Composable
fun IReaderScaffold(
    navController: NavHostController,
    books: List<Book>,
    onImportClick: () -> Unit
) {
    var currentRoute by remember {
        mutableStateOf("bookshelf")
    }
    
    // 监听导航变化
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    navBackStackEntry?.destination?.route?.let { route ->
        currentRoute = route.split("/")[0]
    }
    
    Scaffold(
        bottomBar = {
            IReaderBottomNavigation(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    when (route) {
                        "bookshelf" -> navController.navigate("bookshelf") { 
                            popUpTo(navController.graph.startDestinationId) { saveState = true } 
                        }
                        "reader" -> if (books.isNotEmpty()) navController.navigate("reader/${books[0].id}")
                        "bookmarks" -> navController.navigate("bookmarks")
                        "notes" -> navController.navigate("notes")
                        "settings" -> navController.navigate("settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "bookshelf",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("bookshelf") {
                BookShelfScreen(
                    books = books,
                    onBookClick = { bookId ->
                        navController.navigate("reader/$bookId")
                    },
                    onImportClick = onImportClick
                )
            }
            composable("reader/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                val book = books.find { it.id == bookId }
                if (book != null) {
                    ReaderScreen(book = book, navController = navController)
                }
            }
            composable("bookmarks") {
                BookmarksScreen(navController = navController)
            }
            composable("notes") {
                NotesScreen(navController = navController)
            }
            composable("highlights") {
                HighlightsScreen(navController = navController)
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}