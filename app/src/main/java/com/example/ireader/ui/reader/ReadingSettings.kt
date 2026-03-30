package com.example.ireader.ui.reader

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ireader.R

@Composable
fun ReadingSettingsDialog(
    onDismiss: () -> Unit,
    onFontSizeChanged: (Float) -> Unit,
    onThemeChanged: (Boolean) -> Unit,
    currentFontSize: Float,
    isNightMode: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.reading_settings)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Font Size Slider
                Column {
                    Text(text = stringResource(R.string.font_size))
                    Slider(
                        value = currentFontSize,
                        onValueChange = onFontSizeChanged,
                        valueRange = 12f..24f,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "${currentFontSize.toInt()}sp",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Theme Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.theme))
                    Switch(
                        checked = isNightMode,
                        onCheckedChange = onThemeChanged
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}