package com.project.qmplayer.feature.player

import com.project.qmplayer.domain.model.Song

data class PlaybackQueue(
    val songs: List<Song>,
    val currentIndex: Int
)