package com.project.qmplayer.feature.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlaylistViewModel (
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
): ViewModel() {

    private fun observePlaylistItem(playlist: Playlist): Flow<PlaylistItem> {
        return combine(
            playlistRepository.observeSongUris(playlist.id),
            songRepository.observeSongs()
        ){ songUris, songs ->
            val songsByUri = songs.associateBy { it.uri }
            val playlistSongs = songUris.mapNotNull { uri -> songsByUri[uri] }
            PlaylistItem(playlist, playlistSongs)
        }
    }

    private fun observePlaylistItems(playlists: List<Playlist>): Flow<List<PlaylistItem>> {
        val flows = playlists.map { observePlaylistItem(it) }
        return combine(flows){
            items -> items.toList()
        }
    }

    val uiState: StateFlow<PlaylistUiState> =
        playlistRepository
            .observePlaylists()
            .flatMapLatest { playlists ->
                if(playlists.isEmpty()){
                    flowOf(emptyList())
                }else{
                    observePlaylistItems(playlists)
                }
            }
            .map { playlistItems ->
                PlaylistUiState(
                    playlists = playlistItems,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PlaylistUiState()
            )

    fun createPlaylist(name: String){
        viewModelScope.launch {
            playlistRepository.createPlaylist(name)
        }
    }
}