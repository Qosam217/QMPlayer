package com.project.qmplayer.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongMetaDataRepository
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModel(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val tagRepository: TagRepository,
    private val songMetaDataRepository: SongMetaDataRepository,
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(SongSortOrder.TITLE_ASC)
    val sortOrder: StateFlow<SongSortOrder> = _sortOrder.asStateFlow()

    val uiState: StateFlow<LibraryUiState> =
        combine(songRepository.observeSongs(), _sortOrder) { songs, sortOrder ->
            val sortedSongs = when (sortOrder) {
                SongSortOrder.TITLE_ASC -> songs.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.title })
                SongSortOrder.TITLE_DESC -> songs.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.title })
                SongSortOrder.ARTIST_ASC -> songs.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.artist ?: "" })
                SongSortOrder.DURATION_DESC -> songs.sortedByDescending { it.duration }
            }
            LibraryUiState(
                songs = sortedSongs,
                isLoading = false,
                sortOrder = sortOrder
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LibraryUiState()
        )

    val allPlaylists: StateFlow<List<Playlist>> =
        playlistRepository.observePlaylists()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val playlistSongUris: StateFlow<Map<Long, List<String>>> =
        allPlaylists.flatMapLatest { playlists ->
            if (playlists.isEmpty()) {
                flowOf(emptyMap())
            } else {
                val flows = playlists.map { pl ->
                    playlistRepository.observeSongUris(pl.id)
                }
                combine(flows) { lists ->
                    playlists.indices.associate { i -> playlists[i].id to lists[i] }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyMap()
        )

    val allTags: StateFlow<List<Tag>> =
        tagRepository.observeTags()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun observeTagsForSong(songUri: String): Flow<List<Tag>> {
        return tagRepository.observeTagsForSong(songUri)
    }

    fun toggleSortOrder() {
        val next = when (_sortOrder.value) {
            SongSortOrder.TITLE_ASC -> SongSortOrder.TITLE_DESC
            SongSortOrder.TITLE_DESC -> SongSortOrder.ARTIST_ASC
            SongSortOrder.ARTIST_ASC -> SongSortOrder.DURATION_DESC
            SongSortOrder.DURATION_DESC -> SongSortOrder.TITLE_ASC
        }
        _sortOrder.value = next
    }

    fun addSongToPlaylist(playlistId: Long, songUri: String) {
        viewModelScope.launch {
            val currentUris = playlistSongUris.value[playlistId] ?: emptyList()
            if (!currentUris.contains(songUri)) {
                playlistRepository.addSong(playlistId, songUri, currentUris.size)
            }
        }
    }

    fun createPlaylistAndAddSong(name: String, songUri: String) {
        viewModelScope.launch {
            playlistRepository.createPlaylist(name)
            // Retrieve created playlist by name and add song
            allPlaylists.value.firstOrNull { it.name.equals(name, ignoreCase = true) }?.let { pl ->
                playlistRepository.addSong(pl.id, songUri, 0)
            }
        }
    }

    fun toggleTagForSong(songUri: String, tagId: Long, isAssigned: Boolean) {
        viewModelScope.launch {
            if (isAssigned) {
                tagRepository.addTagToSong(songUri, tagId)
            } else {
                tagRepository.removeTagFromSong(songUri, tagId)
            }
        }
    }

    fun createTagAndAssign(name: String, songUri: String) {
        viewModelScope.launch {
            val tagId = tagRepository.createTag(name)
            if (tagId > 0) {
                tagRepository.addTagToSong(songUri, tagId)
            }
        }
    }

    fun renameSong(songUri: String, newTitle: String) {
        viewModelScope.launch {
            songRepository.renameSong(songUri, newTitle)
        }
    }

    fun deleteSong(song: Song) {
        viewModelScope.launch {
            // Delete from MediaStore
            songRepository.deleteSong(song.uri)
            // Cleanup metadata and relations
            songMetaDataRepository.deleteMetadata(song.uri)
            tagRepository.removeAllTagsFromSong(song.uri)
            playlistRepository.removeSongFromAllPlaylists(song.uri)
        }
    }
}
