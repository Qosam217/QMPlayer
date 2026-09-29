package com.project.qmplayer.feature.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongRepository

class PlaylistViewModelFactory(
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
): ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlaylistViewModel::class.java)){
            return PlaylistViewModel(playlistRepository, songRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}