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
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.feature.library.components.SongCard

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (String) -> Unit
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LibraryContent(
        uiState = uiState,
        onSongClick = onSongClick,
        onSongMenuClick = onSongMenuClick,
        modifier = modifier
    )

}

@Composable
private fun LibraryContent(
    uiState: LibraryUiState,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (String) -> Unit,
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
                            onMenuClick = { onSongMenuClick(song.uri) },
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