package com.project.qmplayer.feature.playlist

import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.model.Song

data class PlaylistItem (
    val playlist: Playlist,
    val songs: List<Song>
)