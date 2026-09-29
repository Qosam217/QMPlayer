package com.project.qmplayer.feature.playlist

data class PlaylistUiState(
    val playlists: List<PlaylistItem> = emptyList(),
    val isLoading: Boolean = true
)