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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
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
import com.project.qmplayer.domain.model.Playlist

@Composable
fun AddToPlaylistDialog(
    playlists: List<Playlist>,
    playlistSongUris: Map<Long, List<String>>,
    songUri: String,
    onAddToPlaylist: (playlistId: Long) -> Unit,
    onCreatePlaylistAndAdd: (name: String) -> Unit,
    onDismiss: () -> Unit
){
    var showCreateField by remember { mutableStateOf(false) }
    var newPlaylistName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to Playlist") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (showCreateField) {
                    val trimmed = newPlaylistName.trim()
                    val isDuplicate = playlists.any { it.name.equals(trimmed, ignoreCase = true) }
                    OutlinedTextField(
                        value = newPlaylistName,
                        onValueChange = { newPlaylistName = it },
                        label = { Text("New playlist name") },
                        singleLine = true,
                        isError = isDuplicate,
                        supportingText = if (isDuplicate) {
                            { Text("Playlist name already exists", color = MaterialTheme.colorScheme.error) }
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
                                onCreatePlaylistAndAdd(trimmed)
                                newPlaylistName = ""
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
                            text = "  Create New Playlist",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (playlists.isEmpty() && !showCreateField) {
                    Text(
                        text = "No playlists created yet.",
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
                        items(playlists, key = { it.id }) { playlist ->
                            val alreadyInPlaylist = playlistSongUris[playlist.id]?.contains(songUri) == true
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !alreadyInPlaylist) {
                                        onAddToPlaylist(playlist.id)
                                    }
                                    .padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = playlist.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (alreadyInPlaylist) {
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        } else {
                                            MaterialTheme.colorScheme.onSurface
                                        }
                                    )
                                    val count = playlistSongUris[playlist.id]?.size ?: 0
                                    Text(
                                        text = "$count songs",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (alreadyInPlaylist) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Added",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = " Added",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
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

