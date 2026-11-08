package com.project.qmplayer.feature.tag.components

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
import com.project.qmplayer.core.ui.util.formatDuration
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.model.Tag

@Composable
fun TagInfoDialog(
    tag: Tag,
    songs: List<Song>,
    onDismiss: () -> Unit
){
    val totalDurationMs = songs.sumOf { it.duration }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tag Details") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InfoRow("Tag Name", tag.name)
                InfoRow("Song Count", "${songs.size} songs")
                InfoRow("Total Duration", formatDuration(totalDurationMs))
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

