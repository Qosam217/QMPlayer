package com.project.qmplayer.feature.library.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.qmplayer.domain.model.Tag

@Composable
fun ManageSongTagsDialog(
    allTags: List<Tag>,
    assignedTagIds: Set<Long>,
    onToggleTag: (tagId: Long, isAssigned: Boolean) -> Unit,
    onCreateTagAndAssign: (name: String) -> Unit,
    onDismiss: () -> Unit
){
    var showCreateField by remember { mutableStateOf(false) }
    var newTagName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tags") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (showCreateField) {
                    val trimmed = newTagName.trim()
                    val isDuplicate = allTags.any { it.name.equals(trimmed, ignoreCase = true) }
                    OutlinedTextField(
                        value = newTagName,
                        onValueChange = { newTagName = it },
                        label = { Text("New tag name") },
                        singleLine = true,
                        isError = isDuplicate,
                        supportingText = if (isDuplicate) {
                            { Text("Tag already exists", color = MaterialTheme.colorScheme.error) }
                        } else null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showCreateField = false }) {
                            Text("Cancel")
                        }
                        TextButton(
                            enabled = trimmed.isNotEmpty() && !isDuplicate,
                            onClick = {
                                onCreateTagAndAssign(trimmed)
                                newTagName = ""
                                showCreateField = false
                            }
                        ) {
                            Text("Create & Add")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showCreateField = true }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        Text(
                            text = "  Create New Tag",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (allTags.isEmpty() && !showCreateField) {
                    Text(
                        text = "No tags created yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 260.dp)
                    ) {
                        items(allTags, key = { it.id }) { tag ->
                            val isAssigned = tag.id in assignedTagIds
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onToggleTag(tag.id, !isAssigned)
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tag.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                Checkbox(
                                    checked = isAssigned,
                                    onCheckedChange = { checked ->
                                        onToggleTag(tag.id, checked)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

