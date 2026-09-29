package com.project.qmplayer.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.qmplayer.domain.repository.SongRepository

class LibraryViewModelFactory(
    private val songRepository: SongRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java)) {
            return LibraryViewModel(songRepository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}