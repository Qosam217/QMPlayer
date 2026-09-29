package com.project.qmplayer.feature.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository

class TagViewModelFactory (
    private val tagRepository: TagRepository,
    private val songRepository: SongRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TagViewModel::class.java)){
            return TagViewModel(tagRepository, songRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}