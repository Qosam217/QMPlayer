package com.project.qmplayer.data.repository

import com.project.qmplayer.data.local.dao.SongMetaDataDao
import com.project.qmplayer.data.local.entity.SongMetaDataEntity
import com.project.qmplayer.domain.model.SongMetaData
import com.project.qmplayer.domain.repository.SongMetaDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongMetaDataRepositoryImpl(
    private val dao: SongMetaDataDao
): SongMetaDataRepository {
    override fun observeSongMetaData(songUri: String): Flow<SongMetaData?>{
        return dao.observeBySongUri(songUri)
            .map { entity ->
                entity?.toDomain()
            }
    }

    override suspend fun saveMetadata(metaData: SongMetaData) {
        dao.upsert(
            SongMetaDataEntity(
                songUri = metaData.songUri,
                customCoverUri = metaData.customCoverUri,
                lyricTxtUri = metaData.lyricTxtUri
            )
        )
    }

    override suspend fun deleteMetadata(songUri: String) {
        dao.delete(
            SongMetaDataEntity(
                songUri = songUri,
                customCoverUri = null,
                lyricTxtUri = null
            )
        )
    }

    private fun SongMetaDataEntity.toDomain(): SongMetaData{
        return SongMetaData(
            songUri = songUri,
            customCoverUri = customCoverUri,
            lyricTxtUri = lyricTxtUri
        )
    }
}