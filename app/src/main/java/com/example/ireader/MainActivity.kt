package com.example.ireader

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ireader.ui.navigation.AppNavigation
import com.example.ireader.ui.navigation.BottomNavigationBar
import com.example.ireader.ui.theme.IReaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val addBookLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
        // 处理返回结果，刷新书架
        // 书架会自动通过Flow刷新
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        onAddBookClick = {
                            val intent = Intent(this@MainActivity, com.example.ireader.ui.bookshelf.FilePickerActivity::class.java)
                            addBookLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(onAddBookClick: () -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        AppNavigation(
            modifier = Modifier.padding(padding),
            navController = navController,
            onAddBookClick = onAddBookClick
        )
    }
}
