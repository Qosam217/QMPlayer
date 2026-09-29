package com.project.qmplayer.data.repository

import com.project.qmplayer.data.local.dao.PlaylistDao
import com.project.qmplayer.data.local.dao.PlaylistSongDao
import com.project.qmplayer.data.local.entity.PlaylistEntity
import com.project.qmplayer.data.local.entity.PlaylistSongEntity
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistSongDao: PlaylistSongDao
): PlaylistRepository {

    override fun observePlaylists(): Flow<List<Playlist>> {
        return playlistDao.observeAll()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun observePlaylist(id: Long): Flow<Playlist?> {
        return playlistDao.observeById(id).map { it.toDomain() }
    }

    override fun observeSongUris(playlistId: Long): Flow<List<String>> {
        return playlistSongDao.observeSongsInPlaylist(playlistId)
            .map { songs -> songs.map { it.songUri } }
    }

    override suspend fun createPlaylist(name: String) {
        playlistDao.insert(PlaylistEntity(
            name = name
        ))
    }

    override suspend fun renamePlaylist(playlist: Playlist) {
        playlistDao.update(PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            createdAt = playlist.createdAt
        ))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.delete(PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            createdAt = playlist.createdAt
        ))
    }

    override suspend fun addSong(playlistId: Long, songUri: String, position: Int) {
        playlistSongDao.insert(PlaylistSongEntity(
            playlistId = playlistId,
            songUri = songUri,
            position = position
        ))
    }

    override suspend fun removeSong(playlistId: Long, songUri: String) {
        playlistSongDao.delete(
            playlistId = playlistId,
            songUri = songUri
        )
    }

    override suspend fun clearPlaylist(playlistId: Long) {
        playlistSongDao.deleteAllSongsInPlaylist(playlistId)
    }

    private fun PlaylistEntity.toDomain(): Playlist{
        return Playlist(
            id = id,
            name = name,
            createdAt = createdAt
        )
    }

    private fun Playlist.toEntity(): PlaylistEntity{
        return PlaylistEntity(
            id = id,
            name = name,
            createdAt = createdAt
        )
    }
}