package com.example.ireader.ui.bookshelf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ireader.ui.theme.IReaderTheme
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FilePickerActivity : ComponentActivity() {

    private val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            // 授予持久权限
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, flags)
            viewModel.importFile(uri)
        }
    }

    private val viewModel: FilePickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IReaderTheme {
                FilePickerScreen(
                    viewModel = viewModel,
                    onImportClick = { openFilePicker() },
                    onScanClick = { viewModel.scanLocalFiles() },
                    onConfirm = {
                        viewModel.importSelectedFiles()
                        setResult(Activity.RESULT_OK)
                        finish()
                    },
                    onCancel = {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }

    private fun openFilePicker() {
        getContent.launch(arrayOf("text/plain", "application/epub+zip", "application/pdf"))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilePickerScreen(
    viewModel: FilePickerViewModel,
    onImportClick: () -> Unit,
    onScanClick: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val foundFiles by viewModel.foundFiles.collectAsStateWithLifecycle()
    val selectedFiles by viewModel.selectedFiles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("选择书籍") },
                navigationIcon = {
                    Text("取消", modifier = Modifier
                        .clickable { onCancel() }
                        .padding(16.dp))
                },
                actions = {
                    Text("确定 (${selectedFiles.size})", modifier = Modifier
                        .clickable(enabled = selectedFiles.isNotEmpty()) { onConfirm() }
                        .padding(16.dp))
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 操作按钮区
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "添加书籍",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onImportClick() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("从文件选择器选择")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onScanClick() }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("扫描存储空间")
                    }
                }
            }

            // 文件列表
            if (foundFiles.isNotEmpty()) {
                Text(
                    text = "找到 ${foundFiles.size} 本书",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                LazyColumn(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(foundFiles) { file ->
                        FileItem(
                            file = file,
                            isSelected = selectedFiles.contains(file),
                            onToggle = { viewModel.toggleSelection(file) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FileItem(
    file: File,
    isSelected: Boolean,
    onToggle: (File) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(file) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = file.name, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = file.absolutePath,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Checkbox(checked = isSelected, onCheckedChange = { onToggle(file) })
        }
    }
}
