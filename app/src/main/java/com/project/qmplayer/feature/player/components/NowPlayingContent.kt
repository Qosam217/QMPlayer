package com.project.qmplayer.feature.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.qmplayer.domain.model.Song

/**
 * "On Playing" page — shows the current playback queue in order.
 * [currentQueueIndex] is highlighted as the playing item.
 * [onBackClick] navigates back to the Music Player page (swipe or button).
 */
@Composable
fun NowPlayingContent(
    songs: List<Song>,
    currentQueueIndex: Int,
    onSongClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    // Scroll to the currently playing item when the list is first shown or the index changes.
    LaunchedEffect(currentQueueIndex, songs.size) {
        if (songs.isNotEmpty() && currentQueueIndex in songs.indices) {
            listState.animateScrollToItem(currentQueueIndex)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // ── Header ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to player",
                )
            }
            Text(
                text = "On Playing",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        // ── Queue list ──
        if (songs.isEmpty()) {
            Text(
                text = "Queue is empty",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
            )
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(
                    items = songs,
                    key = { _, song -> song.uri },
                ) { index, song ->
                    QueueSongItem(
                        song = song,
                        isPlaying = index == currentQueueIndex,
                        onClick = { onSongClick(index) },
                    )
                }
            }
        }
    }
}
