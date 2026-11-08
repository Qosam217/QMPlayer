package com.project.qmplayer.feature.library

import com.project.qmplayer.domain.model.Song

data class LibraryUiState (
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = true,
    val sortOrder: SongSortOrder = SongSortOrder.TITLE_ASC
)
