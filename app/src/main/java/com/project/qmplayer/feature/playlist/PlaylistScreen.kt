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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.qmplayer.core.ui.components.ConfirmDeleteDialog
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.feature.playlist.components.CreatePlaylistDialog
import com.project.qmplayer.feature.playlist.components.PlaylistCard
import com.project.qmplayer.feature.playlist.components.PlaylistInfoDialog
import com.project.qmplayer.feature.playlist.components.PlaylistOptionModal

@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel,
    modifier: Modifier = Modifier,
    onPlaylistClick: (Long) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    var expandedPlaylistByIds by remember { mutableStateOf(emptySet<Long>()) }
    var selectedPlaylistItem by remember { mutableStateOf<PlaylistItem?>(null) }
    var playlistToRename by remember { mutableStateOf<Playlist?>(null) }
    var playlistToDelete by remember { mutableStateOf<Playlist?>(null) }
    var playlistForInfo by remember { mutableStateOf<PlaylistItem?>(null) }

    val existingNames = remember(uiState.playlists) {
        uiState.playlists.map { it.playlist.name }
    }

    when {
        uiState.isLoading -> {
            // Keep existing UI structure
        }

        uiState.playlists.isEmpty() -> {
            EmptyPlaylistContent(modifier = modifier)
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection(
                    title = "Sort: ${sortOrder.label}",
                    onSortClick = { viewModel.toggleSortOrder() },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
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
                                selectedPlaylistItem = item
                            }
                        )
                    }
                }
            }
        }
    }

    selectedPlaylistItem?.let { item ->
        PlaylistOptionModal(
            onDismiss = {
                selectedPlaylistItem = null
            },
            onOptionClick = { option ->
                when (option) {
                    PlaylistOption.PLAY -> {
                        onPlaylistClick(item.playlist.id)
                    }
                    PlaylistOption.RENAME -> {
                        playlistToRename = item.playlist
                    }
                    PlaylistOption.DELETE -> {
                        playlistToDelete = item.playlist
                    }
                    PlaylistOption.INFO -> {
                        playlistForInfo = item
                    }
                }
                selectedPlaylistItem = null
            }
        )
    }

    playlistToRename?.let { playlist ->
        CreatePlaylistDialog(
            existingNames = existingNames,
            initialName = playlist.name,
            title = "Rename Playlist",
            confirmLabel = "Save",
            onConfirm = { newName ->
                viewModel.renamePlaylist(playlist, newName)
                playlistToRename = null
            },
            onDismiss = { playlistToRename = null }
        )
    }

    playlistToDelete?.let { playlist ->
        ConfirmDeleteDialog(
            title = "Delete Playlist",
            message = "Are you sure you want to delete '${playlist.name}'?",
            onConfirm = {
                viewModel.deletePlaylist(playlist)
                playlistToDelete = null
            },
            onDismiss = { playlistToDelete = null }
        )
    }

    playlistForInfo?.let { item ->
        PlaylistInfoDialog(
            playlist = item.playlist,
            songs = item.songs,
            onDismiss = { playlistForInfo = null }
        )
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
        Text("No playlists found")
    }
}
