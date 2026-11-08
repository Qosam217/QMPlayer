package com.project.qmplayer.feature.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.qmplayer.di.QMPlayerContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Coordinates Player UI state and control commands.
 * Does NOT own or instantiate ExoPlayer — that is Phase 4 (MediaSessionService).
 * All mutable state here is placeholder until Media3 is wired in Phase 4.
 */
class PlayerViewModel(
    @Suppress("UNUSED_PARAMETER") container: QMPlayerContainer
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    // ── Playback controls (placeholder; replaced by MediaController in Phase 4) ──

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }

    fun next() { /* wired to MediaController in Phase 4 */ }

    fun previous() { /* wired to MediaController in Phase 4 */ }

    fun seekTo(position: Long) {
        _uiState.value = _uiState.value.copy(currentPosition = position)
    }

    fun toggleShuffle() {
        _uiState.value = _uiState.value.copy(isShuffleEnabled = !_uiState.value.isShuffleEnabled)
    }

    /** Toggles repeat-all on/off. Repeat-one is not required in v1. */
    fun toggleRepeatAll() {
        val next = when (_uiState.value.playbackMode) {
            PlaybackMode.REPEAT_ALL -> PlaybackMode.NO_REPEAT
            PlaybackMode.NO_REPEAT  -> PlaybackMode.REPEAT_ALL
        }
        _uiState.value = _uiState.value.copy(playbackMode = next)
    }

    // ── Queue jump (placeholder) ──

    fun jumpToQueueItem(index: Int) {
        _uiState.value = _uiState.value.copy(currentQueueIndex = index)
    }

    // ── Factory for manual DI ──

    class Factory(private val container: QMPlayerContainer) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            PlayerViewModel(container) as T
    }
}