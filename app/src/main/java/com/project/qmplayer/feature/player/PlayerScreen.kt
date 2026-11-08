package com.project.qmplayer.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.qmplayer.feature.player.components.MusicPlayerContent
import com.project.qmplayer.feature.player.components.NowPlayingContent
import kotlinx.coroutines.launch

/**
 * Full-screen player.
 *
 * Page layout (HorizontalPager):
 *   0 — Music Player (artwork, controls, seek bar, tags)
 *   1 — On Playing  (current queue list)
 *
 * Swipe right from page 0 → page 1; swipe left from page 1 → page 0.
 * Back from either page returns to Home via [onBackClick].
 */
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 },
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            0 -> MusicPlayerContent(
                uiState = uiState,
                onBackClick = onBackClick,
                onPlayPause = viewModel::togglePlayPause,
                onPrevious = viewModel::previous,
                onNext = viewModel::next,
                onSeek = viewModel::seekTo,
                onShuffle = viewModel::toggleShuffle,
                onRepeatAll = viewModel::toggleRepeatAll,
                onOpenQueue = {
                    scope.launch { pagerState.animateScrollToPage(1) }
                },
            )
            1 -> NowPlayingContent(
                songs = uiState.queue,
                currentQueueIndex = uiState.currentQueueIndex,
                onSongClick = { index -> viewModel.jumpToQueueItem(index) },
                onBackClick = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                },
            )
        }
    }
}
