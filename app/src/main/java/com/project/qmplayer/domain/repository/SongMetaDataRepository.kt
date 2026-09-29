package com.project.qmplayer.domain.repository

import com.project.qmplayer.domain.model.SongMetaData
import kotlinx.coroutines.flow.Flow

interface SongMetaDataRepository {
    fun observeSongMetaData(songUri: String): Flow<SongMetaData?>

    suspend fun saveMetadata(metaData: SongMetaData)

    suspend fun deleteMetadata(songUri: String)
}