package com.project.qmplayer.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import androidx.room3.Upsert
import com.project.qmplayer.data.local.entity.SongMetaDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongMetaDataDao {
    @Query("SELECT * FROM song_metadata WHERE songUri = :songUri")
    fun observeBySongUri(songUri: String): Flow<SongMetaDataEntity?>

    @Query("SELECT * FROM song_metadata")
    fun observeAll(): Flow<List<SongMetaDataEntity>>

    @Upsert
    suspend fun upsert(songMetaDataEntity: SongMetaDataEntity)

    @Delete
    suspend fun delete(songMetaDataEntity: SongMetaDataEntity)
}