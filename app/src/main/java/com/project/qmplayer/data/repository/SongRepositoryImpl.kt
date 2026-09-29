package com.project.qmplayer.data.repository

import com.project.qmplayer.data.media.MediaStoreDataSource
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongRepositoryImpl(
    private val mediaStoreDataSource: MediaStoreDataSource
): SongRepository{

    override fun observeSongByUri(songUri: String): Flow<Song?> {
        return flow{
            emit(mediaStoreDataSource.getSong(songUri))
        }
    }

    override fun observeSongs(): Flow<List<Song>> {
        return mediaStoreDataSource.observeSongs()
    }

    override suspend fun getSong(songUri: String): Song? {
        return mediaStoreDataSource.getSong(songUri)
    }
}