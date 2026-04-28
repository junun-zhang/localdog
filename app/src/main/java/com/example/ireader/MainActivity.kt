package com.example.ireader

import android.content.Intent
import android.net.Uri
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val addBookLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) {
        // 处理返回结果，刷新书架
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.data != null) {
            val uri = intent.data!!
            Timber.d("收到文件Intent: $uri")
            val bookScanner = (application as IReaderApplication).bookScanner
            CoroutineScope(Dispatchers.Main).launch {
                val book = bookScanner.importBookFromUri(uri)
                if (book != null) {
                    Timber.d("导入成功: ${book.title}")
                } else {
                    Timber.d("导入失败或文件不支持")
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
