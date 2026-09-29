package com.project.qmplayer.feature.player.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun MusicPlayerContent(
    uiState: PlayerUiState,
    onBackClick: () -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Long) -> Unit,
    onShuffle: () -> Unit,
    onPlaybackMode: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlayerTopBar(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(32.dp))

        PlayerArtwork(artworkUri = uiState.currentSong?.artworkUri)

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = uiState.currentSong?.title ?: "No Song Playing",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = uiState.currentSong?.artist ?: "Unknown Artist",
            style = MaterialTheme.typography.headlineMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        PlayerSeekBar(
            position = uiState.currentPosition,
            duration = uiState.duration,
            onSeek = onSeek
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
            onPlaybackMode = onPlaybackMode
        )
    }
}

@Composable
private fun PlayerTopBar(onBackClick: () -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            text = "Now Playing",
            style = MaterialTheme.typography.titleLarge
        )
    }
}