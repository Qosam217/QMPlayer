package com.project.qmplayer.feature.tag

data class TagUiState (
    val tags: List<TagItem> = emptyList(),
    val isLoading: Boolean = false,
)