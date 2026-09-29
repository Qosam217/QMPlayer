package com.project.qmplayer.feature.tag

import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.model.Tag

data class TagItem (
    val tag: Tag,
    val songs: List<Song>
)