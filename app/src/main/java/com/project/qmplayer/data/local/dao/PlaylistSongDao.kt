package com.project.qmplayer.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.project.qmplayer.data.local.entity.PlaylistSongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistSongDao {
    @Query("""
        SELECT * FROM playlist_song
        WHERE playlistId = :playlistId
        ORDER BY position ASC
    """)
    fun observeSongsInPlaylist(playlistId: Long): Flow<List<PlaylistSongEntity>>

    @Insert
    suspend fun insert(playlistSongs: PlaylistSongEntity)

    @Update
    suspend fun update(playlistSong: PlaylistSongEntity)

    @Delete
    suspend fun delete(playlistSong: PlaylistSongEntity)

    @Query("""
        DELETE FROM playlist_song
        WHERE playlistId = :playlistId
        AND songUri = :songUri
    """)
    suspend fun delete(playlistId: Long, songUri: String)

    @Query("""
        DELETE FROM playlist_song
        WHERE playlistId = :playlistId
    """)
    suspend fun deleteAllSongsInPlaylist(playlistId: Long)
}