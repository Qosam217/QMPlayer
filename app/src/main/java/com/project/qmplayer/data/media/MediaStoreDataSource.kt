package com.project.qmplayer.data.media

import com.project.qmplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface MediaStoreDataSource{
    fun observeSongs(): Flow<List<Song>>

    suspend fun getSong(songUri: String): Song?
}