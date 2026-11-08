package com.project.qmplayer.feature.tag.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun CreateTagDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    existingNames: List<String> = emptyList(),
    initialName: String = "",
    title: String = "New Tag",
    confirmLabel: String = "Create"
){
    var name by remember { mutableStateOf(initialName) }
    var isSubmitting by remember { mutableStateOf(false) }

    val trimmedName = name.trim()
    val isDuplicate = existingNames.any {
        !it.equals(initialName.trim(), ignoreCase = true) && it.equals(trimmedName, ignoreCase = true)
    }
    val errorMessage = when {
        isDuplicate -> "A tag with this name already exists"
        name.isNotEmpty() && trimmedName.isEmpty() -> "Name cannot be only spaces"
        else -> null
    }
    val isConfirmEnabled = trimmedName.isNotEmpty() && !isDuplicate && !isSubmitting

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tag name") },
                singleLine = true,
                isError = errorMessage != null,
                supportingText = errorMessage?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
        },
        confirmButton = {
            TextButton(
                enabled = isConfirmEnabled,
                onClick = {
                    if (isConfirmEnabled) {
                        isSubmitting = true
                        onConfirm(trimmedName)
                    }
                }
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}
