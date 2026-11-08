package com.project.qmplayer.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongMetaDataRepository
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository

class LibraryViewModelFactory(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val tagRepository: TagRepository,
    private val songMetaDataRepository: SongMetaDataRepository,
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(
                songRepository = songRepository,
                playlistRepository = playlistRepository,
                tagRepository = tagRepository,
                songMetaDataRepository = songMetaDataRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
