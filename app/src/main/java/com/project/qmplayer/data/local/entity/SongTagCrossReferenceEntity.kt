package com.project.qmplayer.data.local.entity

import androidx.room3.Entity

@Entity(
    tableName = "song_tag_cross_ref",
    primaryKeys = ["songUri", "tagId"]
)
data class SongTagCrossRef(
    val songUri: String,
    val tagId: Long
)
