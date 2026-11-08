package com.project.qmplayer.feature.library.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun RenameSongDialog(
    initialTitle: String,
    onConfirm: (newTitle: String) -> Unit,
    onDismiss: () -> Unit
){
    var title by remember { mutableStateOf(initialTitle) }
    var isSubmitting by remember { mutableStateOf(false) }

    val trimmed = title.trim()
    val isError = title.isNotEmpty() && trimmed.isEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Song") },
        text = {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Song title") },
                singleLine = true,
                isError = isError,
                supportingText = if (isError) {
                    { Text("Title cannot be blank", color = MaterialTheme.colorScheme.error) }
                } else null
            )
        },
        confirmButton = {
            TextButton(
                enabled = trimmed.isNotEmpty() && !isSubmitting,
                onClick = {
                    if (trimmed.isNotEmpty()) {
                        isSubmitting = true
                        onConfirm(trimmed)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

