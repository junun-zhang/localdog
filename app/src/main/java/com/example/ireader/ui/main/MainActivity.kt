package com.example.ireader.ui.main

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ireader.ui.bookshelf.BookShelfScreen
import com.example.ireader.ui.bookmarks.BookmarksScreen
import com.example.ireader.ui.highlights.HighlightsScreen
import com.example.ireader.ui.notes.NotesScreen
import com.example.ireader.ui.reader.ReaderScreen
import com.example.ireader.ui.settings.SettingsScreen
import com.example.ireader.ui.theme.IReaderTheme

class MainActivity : ComponentActivity() {
    
    private val filePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let { 
            val viewModel = ViewModelProvider(this).get(BooksViewModel::class.java)
            viewModel.addBookFromUri(uri)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderTheme {
                val navController = rememberNavController()
                
                Surface(Modifier.fillMaxSize()) {
                    NavHost(navController, "bookshelf") {
                        composable("bookshelf") {
                            BookShelfScreen(
                                onBookClick = { bookId -> navController.navigate("reader/$bookId") },
                                onImportClick = { filePicker.launch(arrayOf("*/*")) }
                            )
                        }
                        composable("reader/{bookId}") { backStackEntry ->
                            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
                            ReaderScreen(bookId, navController)
                        }
                        composable("bookmarks") { BookmarksScreen(navController) }
                        composable("notes") { NotesScreen(navController) }
                        composable("highlights") { HighlightsScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}