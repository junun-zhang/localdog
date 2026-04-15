package com.example.ireader.ui.reader.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ireader.R
import com.example.ireader.data.model.HighlightColor
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.unit.Dp

@Composable
fun HighlightDialog(
    selectedText: String,
    onConfirm: (HighlightColor) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.highlight_title))
        },
        text = {
            Column(modifier = modifier) {
                Text(
                    text = stringResource(R.string.highlight_selected_text),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$selectedText\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.choose_highlight_color),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                HighlightColor.entries.forEach { color ->
                    Button(
                        onClick = { onConfirm(color) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color.toColor()
                        )
                    ) {
                        Text(" ")
                    }
                    if (color != HighlightColor.entries.last()) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}