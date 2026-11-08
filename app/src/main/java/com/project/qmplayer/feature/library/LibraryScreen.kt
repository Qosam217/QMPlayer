package com.project.qmplayer.feature.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.feature.library.components.AddToPlaylistDialog
import com.project.qmplayer.feature.library.components.ManageSongTagsDialog
import com.project.qmplayer.feature.library.components.RenameSongDialog
import com.project.qmplayer.feature.library.components.SongCard
import com.project.qmplayer.feature.library.components.SongInfoDialog
import com.project.qmplayer.feature.library.components.SongOptionModal

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (Song) -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val playlists by viewModel.allPlaylists.collectAsStateWithLifecycle()
    val playlistSongUris by viewModel.playlistSongUris.collectAsStateWithLifecycle()
    val allTags by viewModel.allTags.collectAsStateWithLifecycle()

    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var songForAddToPlaylist by remember { mutableStateOf<Song?>(null) }
    var songForManageTags by remember { mutableStateOf<Song?>(null) }
    var songForRename by remember { mutableStateOf<Song?>(null) }
    var songForDelete by remember { mutableStateOf<Song?>(null) }
    var songForInfo by remember { mutableStateOf<Song?>(null) }

    LibraryContent(
        uiState = uiState,
        onSortClick = { viewModel.toggleSortOrder() },
        onSongClick = onSongClick,
        onSongMenuClick = { song ->
            selectedSong = song
            onSongMenuClick(song)
        },
        modifier = modifier
    )

    selectedSong?.let { song ->
        SongOptionModal(
            onDismiss = {
                selectedSong = null
            },
            onOptionClick = { option ->
                when (option) {
                    SongOption.PLAY -> {
                        onSongClick(song.uri)
                    }
                    SongOption.ADD_TO_PLAYLIST -> {
                        songForAddToPlaylist = song
                    }
                    SongOption.ADD_TAG -> {
                        songForManageTags = song
                    }
                    SongOption.RENAME -> {
                        songForRename = song
                    }
                    SongOption.DELETE -> {
                        songForDelete = song
                    }
                    SongOption.INFO -> {
                        songForInfo = song
                    }
                }
                selectedSong = null
            }
        )
    }

    songForAddToPlaylist?.let { song ->
        AddToPlaylistDialog(
            playlists = playlists,
            playlistSongUris = playlistSongUris,
            songUri = song.uri,
            onAddToPlaylist = { playlistId ->
                viewModel.addSongToPlaylist(playlistId, song.uri)
                songForAddToPlaylist = null
            },
            onCreatePlaylistAndAdd = { name ->
                viewModel.createPlaylistAndAddSong(name, song.uri)
                songForAddToPlaylist = null
            },
            onDismiss = { songForAddToPlaylist = null }
        )
    }

    songForManageTags?.let { song ->
        val songTags by viewModel.observeTagsForSong(song.uri).collectAsStateWithLifecycle(initialValue = emptyList())
        val assignedTagIds = remember(songTags) { songTags.map { it.id }.toSet() }

        ManageSongTagsDialog(
            allTags = allTags,
            assignedTagIds = assignedTagIds,
            onToggleTag = { tagId, isAssigned ->
                viewModel.toggleTagForSong(song.uri, tagId, isAssigned)
            },
            onCreateTagAndAssign = { name ->
                viewModel.createTagAndAssign(name, song.uri)
            },
            onDismiss = { songForManageTags = null }
        )
    }

    songForRename?.let { song ->
        RenameSongDialog(
            initialTitle = song.title,
            onConfirm = { newTitle ->
                viewModel.renameSong(song.uri, newTitle)
                songForRename = null
            },
            onDismiss = { songForRename = null }
        )
    }

    songForDelete?.let { song ->
        ConfirmDeleteDialog(
            title = "Delete Song",
            message = "Are you sure you want to delete '${song.title}' from your device?",
            onConfirm = {
                viewModel.deleteSong(song)
                songForDelete = null
            },
            onDismiss = { songForDelete = null }
        )
    }

    songForInfo?.let { song ->
        val songTags by viewModel.observeTagsForSong(song.uri).collectAsStateWithLifecycle(initialValue = emptyList())
        SongInfoDialog(
            song = song,
            tags = songTags,
            onDismiss = { songForInfo = null }
        )
    }
}

@Composable
private fun LibraryContent(
    uiState: LibraryUiState,
    onSortClick: () -> Unit,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (Song) -> Unit,
    modifier: Modifier = Modifier,
){
    when{
        uiState.isLoading -> {
            LoadingContent(modifier = modifier)
        }

        uiState.songs.isEmpty() -> {
            EmptyLibraryContent(modifier = modifier)
        } else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection(
                    title = "Sort: ${uiState.sortOrder.label}",
                    onSortClick = onSortClick,
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.songs,
                        key = { song -> song.uri }
                    ){ song ->
                        SongCard(
                            song = song,
                            onClick = { onSongClick(song.uri) },
                            onMenuClick = { onSongMenuClick(song) },
                            modifier = Modifier.height(72.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyLibraryContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No songs found")
    }
}
