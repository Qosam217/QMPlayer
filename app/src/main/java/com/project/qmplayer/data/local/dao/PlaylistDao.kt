package com.project.qmplayer.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.project.qmplayer.data.local.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao{
    @Query("SELECT * FROM playlists")
    fun observeAll(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun observeById(id: Long): Flow<PlaylistEntity?>

    @Insert
    suspend fun insert(playlistEntity: PlaylistEntity)

    @Update
    suspend fun update(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun delete(playlistEntity: PlaylistEntity)
}