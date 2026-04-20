package com.example.ireader.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    var fontSize by remember { mutableStateOf(16f) }
    var isDarkTheme by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text("字体大小", style = MaterialTheme.typography.titleMedium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Slider(value = fontSize, onValueChange = { fontSize = it }, valueRange = 12f..32f, modifier = Modifier.weight(1f))
                        Text("${fontSize.toInt()}sp", Modifier.padding(start = 8.dp))
                    }
                }
            }
            Card {
                Row(Modifier.padding(16.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text("深色主题", style = MaterialTheme.typography.titleMedium)
                    Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
                }
            }
        }
    }
}