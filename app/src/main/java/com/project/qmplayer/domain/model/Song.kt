package com.project.qmplayer.domain.model

data class Song(
    val uri: String,
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val artworkUri: String?
)