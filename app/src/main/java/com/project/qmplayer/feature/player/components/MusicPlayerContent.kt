package com.project.qmplayer.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.qmplayer.feature.player.PlayerUiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MusicPlayerContent(
    uiState: PlayerUiState,
    onBackClick: () -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Long) -> Unit,
    onShuffle: () -> Unit,
    onRepeatAll: () -> Unit,
    onOpenQueue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PlayerTopBar(onBackClick = onBackClick, onOpenQueue = onOpenQueue)

        Spacer(modifier = Modifier.height(32.dp))

        PlayerArtwork(artworkUri = uiState.currentSong?.artworkUri)

        Spacer(modifier = Modifier.height(28.dp))

        // Title
        Text(
            text = uiState.currentSong?.title ?: "No Song Playing",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Artist
        Text(
            text = uiState.currentSong?.artist ?: "Unknown Artist",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        // Tags row (shows only when tags are available)
        if (uiState.currentSongTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                uiState.currentSongTags.forEach { tag ->
                    AssistChip(
                        onClick = {},
                        label = { Text(tag.name) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PlayerSeekBar(
            position = uiState.currentPosition,
            duration = uiState.duration,
            onSeek = onSeek,
        )

        Spacer(modifier = Modifier.height(20.dp))

        PlayerControls(
            isPlaying = uiState.isPlaying,
            isShuffleEnabled = uiState.isShuffleEnabled,
            playbackMode = uiState.playbackMode,
            onPrevious = onPrevious,
            onPlayPause = onPlayPause,
            onNext = onNext,
            onShuffle = onShuffle,
            onRepeatAll = onRepeatAll,
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PlayerTopBar(
    onBackClick: () -> Unit,
    onOpenQueue: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
            )
        }
        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onOpenQueue) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                contentDescription = "View queue",
            )
        }
    }
}
