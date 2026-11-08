package com.project.qmplayer.feature.playlist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.qmplayer.core.ui.util.formatDate
import com.project.qmplayer.core.ui.util.formatDuration
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.model.Song

@Composable
fun PlaylistInfoDialog(
    playlist: Playlist,
    songs: List<Song>,
    onDismiss: () -> Unit
){
    val totalDurationMs = songs.sumOf { it.duration }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Playlist Details") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow("Name", playlist.name)
                InfoRow("Song Count", "${songs.size} songs")
                InfoRow("Total Duration", formatDuration(totalDurationMs))
                InfoRow("Created", formatDate(playlist.createdAt))
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

