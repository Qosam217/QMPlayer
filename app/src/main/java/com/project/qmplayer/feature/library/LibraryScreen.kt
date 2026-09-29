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
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.feature.library.components.SongCard
import com.project.qmplayer.feature.library.components.SongOptionModal

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (Song) -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    var selectedSong by remember {
        mutableStateOf<Song?>(null)
    }

    LibraryContent(
        uiState = uiState,
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
                    SongOption.PLAY -> {}
                    SongOption.ADD_TO_PLAYLIST -> {}
                    SongOption.ADD_TAG -> {}
                    SongOption.RENAME -> {}
                    SongOption.DELETE -> {}
                    SongOption.INFO -> {}
                }
                selectedSong = null
            }
        )
    }
}

@Composable
private fun LibraryContent(
    uiState: LibraryUiState,
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
        }else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection("Sort", {}, modifier = Modifier.fillMaxWidth())
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
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