package com.calsync.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("确认退出") },
            text = { Text("确定要退出登录吗？") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("取消")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("提醒通知", fontSize = 16.sp)
                    Text("开启事件提醒", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("深色模式", fontSize = 16.sp)
                    Text("使用深色主题", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = darkThemeEnabled, onCheckedChange = { darkThemeEnabled = it })
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            OutlinedButton(
                onClick = onNavigateToAbout,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("关于 CalSync")
            }

            Spacer(Modifier.weight(1f))

            // Logout button at bottom
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("退出登录")
            }
        }
    }
}
