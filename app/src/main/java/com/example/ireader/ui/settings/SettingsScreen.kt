package com.example.ireader.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ireader.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var fontSize by remember { mutableStateOf(16f) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var brightness by remember { mutableStateOf(0.8f) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.reading_settings),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                SettingItem(
                    title = stringResource(R.string.font_size),
                    value = "${fontSize.toInt()}sp",
                    onValueChange = { fontSize = it }
                )
            }
            
            item {
                SettingItem(
                    title = stringResource(R.string.theme),
                    value = if (isDarkTheme) stringResource(R.string.night_theme) else stringResource(R.string.day_theme),
                    onToggle = { isDarkTheme = !isDarkTheme }
                )
            }
            
            item {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "亮度",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Slider(
                        value = brightness,
                        onValueChange = { brightness = it },
                        valueRange = 0.1f..1.0f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }
            
            item {
                Text(
                    text = "应用信息",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                ListItem(
                    headlineContent = { Text("版本") },
                    supportingContent = { Text("1.0.0") }
                )
            }
            
            item {
                ListItem(
                    headlineContent = { Text("存储位置") },
                    supportingContent = { Text("内部存储") }
                )
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    value: String,
    onValueChange: ((Float) -> Unit)? = null,
    onToggle: (() -> Unit)? = null
) {
    if (onValueChange != null) {
        var fontSizeValue by remember { mutableStateOf(value.replace("sp", "").toFloat()) }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Row {
                IconButton(onClick = { 
                    if (fontSizeValue > 12f) {
                        fontSizeValue -= 2f
                        onValueChange(fontSizeValue)
                    }
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Remove,
                        contentDescription = "减小字体"
                    )
                }
                Text(
                    text = "${fontSizeValue.toInt()}sp",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = { 
                    if (fontSizeValue < 24f) {
                        fontSizeValue += 2f
                        onValueChange(fontSizeValue)
                    }
                }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Add,
                        contentDescription = "增大字体"
                    )
                }
            }
        }
    } else if (onToggle != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = value == "夜间",
                onCheckedChange = { onToggle() }
            )
        }
    }
}