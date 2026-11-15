package com.project.qmplayer.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.di.QMPlayerContainer
import com.project.qmplayer.domain.model.Song
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Coordinates Player UI state and control commands.
 * Integrates with [QMPlayerContainer] to observe song tags and initial library queue.
 * Real audio engine integration is handled in Phase 4 via MediaSessionService/MediaController.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PlayerViewModel(
    private val container: QMPlayerContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var originalQueue: List<Song> = emptyList()

    init {
        // Populate initial queue from local library if empty
        viewModelScope.launch {
            container.songRepository.observeSongs().collect { songs ->
                if (songs.isNotEmpty() && _uiState.value.queue.isEmpty()) {
                    originalQueue = songs
                    val initialSong = songs.firstOrNull()
                    _uiState.update { current ->
                        current.copy(
                            queue = songs,
                            currentQueueIndex = 0,
                            currentSong = initialSong,
                            duration = initialSong?.duration ?: 0L,
                            currentPosition = 0L,
                            canSkipPrevious = calculateCanSkipPrevious(0, songs.size, current.playbackMode),
                            canSkipNext = calculateCanSkipNext(0, songs.size, current.playbackMode),
                        )
                    }
                }
            }
        }

        // Observe tags for the currently active song
        viewModelScope.launch {
            _uiState
                .map { it.currentSong?.uri }
                .distinctUntilChanged()
                .flatMapLatest { uri ->
                    if (uri != null) {
                        container.tagRepository.observeTagsForSong(uri)
                    } else {
                        flowOf(emptyList())
                    }
                }
                .collect { tags ->
                    _uiState.update { it.copy(currentSongTags = tags) }
                }
        }
    }

    // ── Playback controls ──

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun next() {
        _uiState.update { current ->
            if (current.queue.isEmpty()) return@update current
            val nextIndex = if (current.currentQueueIndex + 1 < current.queue.size) {
                current.currentQueueIndex + 1
            } else if (current.playbackMode == PlaybackMode.REPEAT_ALL) {
                0
            } else {
                current.currentQueueIndex
            }
            val song = current.queue.getOrNull(nextIndex)
            current.copy(
                currentQueueIndex = nextIndex,
                currentSong = song,
                duration = song?.duration ?: 0L,
                currentPosition = 0L,
                canSkipPrevious = calculateCanSkipPrevious(nextIndex, current.queue.size, current.playbackMode),
                canSkipNext = calculateCanSkipNext(nextIndex, current.queue.size, current.playbackMode),
            )
        }
    }

    fun previous() {
        _uiState.update { current ->
            if (current.queue.isEmpty()) return@update current
            val prevIndex = if (current.currentQueueIndex > 0) {
                current.currentQueueIndex - 1
            } else if (current.playbackMode == PlaybackMode.REPEAT_ALL) {
                current.queue.size - 1
            } else {
                0
            }
            val song = current.queue.getOrNull(prevIndex)
            current.copy(
                currentQueueIndex = prevIndex,
                currentSong = song,
                duration = song?.duration ?: 0L,
                currentPosition = 0L,
                canSkipPrevious = calculateCanSkipPrevious(prevIndex, current.queue.size, current.playbackMode),
                canSkipNext = calculateCanSkipNext(prevIndex, current.queue.size, current.playbackMode),
            )
        }
    }

    fun seekTo(position: Long) {
        _uiState.update { it.copy(currentPosition = position) }
    }

    fun toggleShuffle() {
        _uiState.update { current ->
            val newShuffle = !current.isShuffleEnabled
            val currentSong = current.currentSong
            val newQueue = if (newShuffle) {
                if (currentSong != null) {
                    val remaining = current.queue.filter { it.uri != currentSong.uri }.shuffled()
                    listOf(currentSong) + remaining
                } else {
                    current.queue.shuffled()
                }
            } else {
                originalQueue.ifEmpty { current.queue }
            }
            val newIndex = if (currentSong != null) {
                newQueue.indexOfFirst { it.uri == currentSong.uri }.coerceAtLeast(0)
            } else {
                0
            }
            current.copy(
                isShuffleEnabled = newShuffle,
                queue = newQueue,
                currentQueueIndex = newIndex,
                canSkipPrevious = calculateCanSkipPrevious(newIndex, newQueue.size, current.playbackMode),
                canSkipNext = calculateCanSkipNext(newIndex, newQueue.size, current.playbackMode),
            )
        }
    }

    /** Toggles repeat-all on/off. Repeat-one is not required in v1. */
    fun toggleRepeatAll() {
        _uiState.update { current ->
            val nextMode = when (current.playbackMode) {
                PlaybackMode.REPEAT_ALL -> PlaybackMode.NO_REPEAT
                PlaybackMode.NO_REPEAT  -> PlaybackMode.REPEAT_ALL
            }
            current.copy(
                playbackMode = nextMode,
                canSkipPrevious = calculateCanSkipPrevious(current.currentQueueIndex, current.queue.size, nextMode),
                canSkipNext = calculateCanSkipNext(current.currentQueueIndex, current.queue.size, nextMode),
            )
        }
    }

    // ── Queue selection ──

    fun jumpToQueueItem(index: Int) {
        _uiState.update { current ->
            if (index !in current.queue.indices) return@update current
            val song = current.queue[index]
            current.copy(
                currentQueueIndex = index,
                currentSong = song,
                duration = song.duration,
                currentPosition = 0L,
                isPlaying = true,
                canSkipPrevious = calculateCanSkipPrevious(index, current.queue.size, current.playbackMode),
                canSkipNext = calculateCanSkipNext(index, current.queue.size, current.playbackMode),
            )
        }
    }

    fun playSong(songUri: String) {
        _uiState.update { current ->
            val index = current.queue.indexOfFirst { it.uri == songUri }
            if (index != -1) {
                val song = current.queue[index]
                current.copy(
                    currentQueueIndex = index,
                    currentSong = song,
                    duration = song.duration,
                    currentPosition = 0L,
                    isPlaying = true,
                    canSkipPrevious = calculateCanSkipPrevious(index, current.queue.size, current.playbackMode),
                    canSkipNext = calculateCanSkipNext(index, current.queue.size, current.playbackMode),
                )
            } else {
                current
            }
        }
    }

    private fun calculateCanSkipPrevious(currentIndex: Int, queueSize: Int, mode: PlaybackMode): Boolean {
        if (queueSize <= 1) return false
        return mode == PlaybackMode.REPEAT_ALL || currentIndex > 0
    }

    private fun calculateCanSkipNext(currentIndex: Int, queueSize: Int, mode: PlaybackMode): Boolean {
        if (queueSize <= 1) return false
        return mode == PlaybackMode.REPEAT_ALL || currentIndex < queueSize - 1
    }

    // ── Factory for manual DI ──

    class Factory(private val container: QMPlayerContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PlayerViewModel(container) as T
    }
}
