package com.project.qmplayer.feature.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class PlaylistSortOrder(val label: String) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    SONG_COUNT_DESC("Most Songs"),
    DATE_DESC("Newest")
}

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistViewModel (
    private val playlistRepository: PlaylistRepository,
    private val songRepository: SongRepository
): ViewModel() {

    private val _sortOrder = MutableStateFlow(PlaylistSortOrder.NAME_ASC)
    val sortOrder: StateFlow<PlaylistSortOrder> = _sortOrder.asStateFlow()

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
        return combine(flows){ items ->
            items.toList()
        }
    }

    val uiState: StateFlow<PlaylistUiState> =
        combine(
            playlistRepository.observePlaylists().flatMapLatest { playlists ->
                if (playlists.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    observePlaylistItems(playlists)
                }
            },
            _sortOrder
        ) { playlistItems, sortOrder ->
            val sorted = when (sortOrder) {
                PlaylistSortOrder.NAME_ASC -> playlistItems.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.playlist.name })
                PlaylistSortOrder.NAME_DESC -> playlistItems.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.playlist.name })
                PlaylistSortOrder.SONG_COUNT_DESC -> playlistItems.sortedByDescending { it.songs.size }
                PlaylistSortOrder.DATE_DESC -> playlistItems.sortedByDescending { it.playlist.createdAt }
            }
            PlaylistUiState(
                playlists = sorted,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlaylistUiState()
        )

    fun toggleSortOrder() {
        val next = when (_sortOrder.value) {
            PlaylistSortOrder.NAME_ASC -> PlaylistSortOrder.NAME_DESC
            PlaylistSortOrder.NAME_DESC -> PlaylistSortOrder.SONG_COUNT_DESC
            PlaylistSortOrder.SONG_COUNT_DESC -> PlaylistSortOrder.DATE_DESC
            PlaylistSortOrder.DATE_DESC -> PlaylistSortOrder.NAME_ASC
        }
        _sortOrder.value = next
    }

    fun createPlaylist(name: String){
        viewModelScope.launch {
            playlistRepository.createPlaylist(name)
        }
    }

    fun renamePlaylist(playlist: Playlist, newName: String) {
        viewModelScope.launch {
            playlistRepository.renamePlaylist(playlist.copy(name = newName))
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlist)
        }
    }
}
