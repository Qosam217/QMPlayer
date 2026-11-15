package com.project.qmplayer

import com.project.qmplayer.core.ui.util.formatDuration
import com.project.qmplayer.domain.model.Song
import com.project.qmplayer.feature.player.PlaybackMode
import com.project.qmplayer.feature.player.PlayerUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormatUtilsTest {

    @Test
    fun formatDuration_zeroOrNegative_returnsZero() {
        assertEquals("0:00", formatDuration(0L))
        assertEquals("0:00", formatDuration(-500L))
    }

    @Test
    fun formatDuration_underOneMinute() {
        assertEquals("0:05", formatDuration(5_000L))
        assertEquals("0:59", formatDuration(59_000L))
    }

    @Test
    fun formatDuration_minutesAndSeconds() {
        assertEquals("3:45", formatDuration(225_000L))
        assertEquals("10:00", formatDuration(600_000L))
    }

    @Test
    fun formatDuration_hoursMinutesSeconds() {
        assertEquals("1:00:00", formatDuration(3600_000L))
        assertEquals("1:23:45", formatDuration(5025_000L))
    }
}

class PlayerUiStateTest {

    @Test
    fun defaultState_isSafe() {
        val state = PlayerUiState()
        assertEquals(null, state.currentSong)
        assertTrue(state.queue.isEmpty())
        assertFalse(state.isPlaying)
        assertEquals(0L, state.currentPosition)
        assertEquals(0L, state.duration)
        assertFalse(state.isShuffleEnabled)
        assertEquals(PlaybackMode.REPEAT_ALL, state.playbackMode)
        assertFalse(state.canSkipPrevious)
        assertFalse(state.canSkipNext)
    }

    @Test
    fun state_withPopulatedQueue() {
        val song1 = Song("uri1", "Song 1", "Artist 1", "Album 1", 180_000L, null)
        val song2 = Song("uri2", "Song 2", "Artist 2", "Album 2", 200_000L, null)
        val state = PlayerUiState(
            currentSong = song1,
            queue = listOf(song1, song2),
            currentQueueIndex = 0,
            isPlaying = true,
            duration = 180_000L,
            canSkipPrevious = false,
            canSkipNext = true
        )

        assertEquals("Song 1", state.currentSong?.title)
        assertEquals(2, state.queue.size)
        assertTrue(state.canSkipNext)
        assertFalse(state.canSkipPrevious)
    }
}

