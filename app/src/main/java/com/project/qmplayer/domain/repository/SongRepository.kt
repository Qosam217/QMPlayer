package com.project.qmplayer.domain.repository

import com.project.qmplayer.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface SongRepository {
    fun observeSongs(): Flow<List<Song>>

    fun observeSongByUri(songUri: String): Flow<Song?>

    suspend fun getSong(songUri: String): Song?

    suspend fun renameSong(songUri: String, newTitle: String): Boolean

    suspend fun deleteSong(songUri: String): Boolean
}
