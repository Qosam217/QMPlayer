package com.project.qmplayer.domain.repository

import com.project.qmplayer.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun observePlaylists(): Flow<List<Playlist>>

    fun observePlaylist(id: Long): Flow<Playlist?>

    fun observeSongUris(playlistId: Long): Flow<List<String>>

    suspend fun createPlaylist(name: String)

    suspend fun renamePlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun addSong(playlistId: Long, songUri: String, position: Int)

    suspend fun removeSong(playlistId: Long, songUri: String)

    suspend fun removeSongFromAllPlaylists(songUri: String)

    suspend fun clearPlaylist(playlistId: Long)
}