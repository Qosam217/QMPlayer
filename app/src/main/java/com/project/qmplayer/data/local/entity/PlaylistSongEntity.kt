package com.project.qmplayer.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index

@Entity(
    tableName = "playlist_song",
    primaryKeys = ["playlistId", "songUri"],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],
        childColumns = ["playlistId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index("playlistId")
    ]
)
data class PlaylistSongEntity(
    val playlistId: Long,
    val songUri: String,
    val position: Int
)