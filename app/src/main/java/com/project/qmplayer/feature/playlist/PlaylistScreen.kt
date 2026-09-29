package com.project.qmplayer.feature.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.feature.playlist.components.PlaylistCard

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    modifier: Modifier = Modifier,
    onPlaylistClick: (Long) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var expandedPlaylistByIds by remember{
        mutableStateOf(emptySet<Long>())
    }

    when {
        uiState.isLoading -> {

        }

        uiState.playlists.isEmpty() -> {
            EmptyPlaylistContent(modifier = modifier)
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection("Sort", {}, modifier = Modifier.fillMaxWidth())

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.playlists,
                        key = { playlist -> playlist.playlist.id }
                    ){ item ->
                        val playlistId = item.playlist.id

                        PlaylistCard(
                            playlist = item.playlist,
                            songs = item.songs,
                            expanded = playlistId in expandedPlaylistByIds,
                            onExpandClick = {
                                expandedPlaylistByIds =
                                    if (playlistId in expandedPlaylistByIds){
                                        expandedPlaylistByIds - playlistId
                                    }else{
                                        expandedPlaylistByIds + playlistId
                                    }
                            },
                            onMenuClick = {
                                onPlaylistClick(playlistId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPlaylistContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No Playlists Found")
    }
}