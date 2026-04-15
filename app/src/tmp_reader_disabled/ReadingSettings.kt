package com.example.ireader.ui.reader

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ReadingSettings(
    val fontSize: Float = 16f,
    val lineHeight: Float = 1.5f,
    val theme: ReadingTheme = ReadingTheme.Light
) {
    enum class ReadingTheme {
        Light, Dark, Sepia
    }
    
    companion object {
        fun getDefaultSettings(): ReadingSettings = ReadingSettings()
    }
}

@Composable
fun ReadingSettings(
    settings: ReadingSettings,
    onSettingsChanged: (ReadingSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("阅读设置") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 字体大小设置
                Column {
                    Text(text = "字体大小")
                    Slider(
                        value = settings.fontSize,
                        onValueChange = { newValue ->
                            onSettingsChanged(settings.copy(fontSize = newValue))
                        },
                        valueRange = 12f..24f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${settings.fontSize.toInt()}sp",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // 行间距设置
                Column {
                    Text(text = "行间距")
                    Slider(
                        value = settings.lineHeight,
                        onValueChange = { newValue ->
                            onSettingsChanged(settings.copy(lineHeight = newValue))
                        },
                        valueRange = 1.0f..2.5f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "%.1fx".format(settings.lineHeight),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // 主题设置
                Column {
                    Text(text = "主题")
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = {}
                    ) {
                        TextField(
                            readOnly = true,
                            value = settings.theme.name,
                            onValueChange = {},
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                            },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = {}
                        ) {
                            ReadingSettings.ReadingTheme.values().forEach { theme ->
                                DropdownMenuItem(
                                    text = { Text(theme.name) },
                                    onClick = {
                                        onSettingsChanged(settings.copy(theme = theme))
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("确定")
            }
        },
        modifier = modifier
    )
}
