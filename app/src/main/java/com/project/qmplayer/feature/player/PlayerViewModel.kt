package com.project.qmplayer.feature.player

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(PlayerUiState())

    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun play(){
        _uiState.value = _uiState.value.copy(isPlaying = true)
    }

    fun pause(){
        _uiState.value = _uiState.value.copy(isPlaying = false)
    }

    fun togglePause(){
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }

    fun next(){}

    fun previous(){}

    fun seekTo(position: Long){
        _uiState.value = _uiState.value.copy(currentPosition = position)
    }

    fun toggleShuffle(){
        _uiState.value = _uiState.value.copy(isShuffleEnabled = !_uiState.value.isShuffleEnabled)
    }

    fun togglePlaybackMode(){
        val current = _uiState.value.playbackMode

        val next = when(current){
            PlaybackMode.REPEAT_ALL -> PlaybackMode.REPEAT_ONE
            PlaybackMode.REPEAT_ONE -> PlaybackMode.REPEAT_ALL
        }

        _uiState.value = _uiState.value.copy(playbackMode = next)
    }
}