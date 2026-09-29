package com.project.qmplayer.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "song_metadata")
data class SongMetaDataEntity(
    @PrimaryKey
    val songUri: String,
    val customCoverUri: String?,
    val lyricTxtUri: String?
)
