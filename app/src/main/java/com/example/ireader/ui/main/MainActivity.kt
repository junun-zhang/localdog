package com.example.ireader.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ireader.data.model.Book
import com.example.ireader.data.repository.BookRepository
import com.example.ireader.ui.bookshelf.BookShelfScreen
import com.example.ireader.ui.components.BottomNavigationItem
import com.example.ireader.ui.components.IReaderBottomNavigation
import com.example.ireader.ui.reader.ReaderScreen
import com.example.ireader.ui.settings.SettingsScreen
import com.example.ireader.ui.theme.IReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var bookRepository: BookRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderApp(bookRepository = bookRepository)
        }
    }
}

@Composable
fun IReaderApp(bookRepository: BookRepository) {
    val navController = rememberNavController()
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    
    // Load books from repository
    LaunchedEffect(Unit) {
        books = bookRepository.getAllBooks()
    }
    
    IReaderTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            IReaderScaffold(navController = navController, books = books, bookRepository = bookRepository)
        }
    }
}

@Composable
fun IReaderScaffold(
    navController: NavHostController,
    books: List<Book>,
    bookRepository: BookRepository
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
            if (currentRoute in listOf("bookshelf", "reader", "settings")) {
                IReaderBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        when (route) {
                            BottomNavigationItem.Bookshelf.route -> navController.navigate("bookshelf") { popUpTo(navController.graph.startDestinationId) { saveState = true } }
                            BottomNavigationItem.Reader.route -> if (books.isNotEmpty()) navController.navigate("reader/${books[0].id}")
                            BottomNavigationItem.Settings.route -> navController.navigate("settings")
                        }
                    }
                )
            }
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
                    onImportClick = {
                        // TODO: 实现导入功能
                    },
                    onRefresh = {
                        // Refresh books from repository
                        // This will be handled by the ViewModel in future
                    }
                )
            }
            composable("reader/{bookId}") { backStackEntry ->
                val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                val book = books.find { it.id == bookId }
                if (book != null) {
                    ReaderScreen(book = book, navController = navController)
                }
            }
            composable("settings") {
                SettingsScreen(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IReaderTheme {
        // For preview, use sample data
        val sampleBooks = listOf(
            Book(
                id = "1",
                title = "示例书籍",
                author = "作者",
                coverUri = null,
                filePath = "/path/to/book.epub",
                fileSize = 1000000L,
                lastReadTime = System.currentTimeMillis(),
                progress = 0,
                format = "EPUB"
            )
        )
        IReaderScaffold(
            navController = rememberNavController(),
            books = sampleBooks,
            bookRepository = object : BookRepository {
                override suspend fun getAllBooks(): List<Book> = sampleBooks
                override suspend fun getBookById(id: String): Book? = sampleBooks.firstOrNull()
                override suspend fun addBook(book: Book) {}
                override suspend fun updateBook(book: Book) {}
                override suspend fun deleteBook(id: String) {}
                override suspend fun scanLocalFiles(): List<Book> = sampleBooks
            }
        )
    }
}