package com.project.qmplayer.feature.player

import com.project.qmplayer.domain.model.Song

data class PlayerUiState (
    val currentSong: Song? = null,
    val queue: List<Song> = emptyList(),
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.REPEAT_ALL
)