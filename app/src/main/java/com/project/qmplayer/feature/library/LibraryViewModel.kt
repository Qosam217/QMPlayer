package com.project.qmplayer.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LibraryViewModel (
    private val songRepository: SongRepository,
): ViewModel() {

    val uiState: StateFlow<LibraryUiState> =
        songRepository.observeSongs()
            .map { songs ->
                LibraryUiState(
                    songs = songs,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LibraryUiState()
            )

}