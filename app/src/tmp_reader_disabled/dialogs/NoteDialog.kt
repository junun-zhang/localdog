package com.example.ireader.ui.reader.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import com.example.ireader.R

@Composable
fun NoteDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    initialContent: String = ""
) {
    var noteContent by remember { mutableStateOf(initialContent) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.add_note))
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text(stringResource(R.string.note_content)) },
                    placeholder = { Text(stringResource(R.string.enter_your_note_here)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 6,
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (noteContent.isNotBlank()) {
                        onSave(noteContent.trim())
                        onDismiss()
                    }
                },
                enabled = noteContent.isNotBlank()
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}