package com.project.qmplayer.feature.player

import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.model.Tag

data class PlayerUiState(
    val currentSong: Song? = null,
    val currentSongTags: List<Tag> = emptyList(),
    val queue: List<Song> = emptyList(),
    /** Index into [queue] of the item currently playing. */
    val currentQueueIndex: Int = 0,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isShuffleEnabled: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.REPEAT_ALL,
)
