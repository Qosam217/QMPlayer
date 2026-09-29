package com.project.qmplayer.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.qmplayer.feature.player.PlaybackMode

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isShuffleEnabled: Boolean,
    playbackMode: PlaybackMode,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onShuffle: () -> Unit,
    onPlaybackMode: () -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onClick = onShuffle) {
            Icon(imageVector = Icons.Default.Shuffle, contentDescription = null)
        }

        IconButton(onClick = onPrevious) {
            Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = null)
        }

        FilledIconButton(
            onClick = onPlayPause,
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                imageVector = if(isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }

        IconButton(onClick = onNext) {
            Icon(imageVector = Icons.Default.SkipNext, contentDescription = null)
        }

        IconButton(onClick = onPlaybackMode) {
            Icon(
                imageVector = when(playbackMode){
                    PlaybackMode.REPEAT_ALL -> Icons.Default.Repeat
                    PlaybackMode.REPEAT_ONE -> Icons.Default.RepeatOne
                },
                contentDescription = null
            )
        }
    }
}