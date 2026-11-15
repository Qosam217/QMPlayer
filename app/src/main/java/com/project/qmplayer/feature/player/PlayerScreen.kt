package com.project.qmplayer.feature.player

import androidx.activity.compose.BackHandler
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
 *   0 — On Playing (current queue list)
 *   1 — Music Player (artwork, controls, seek bar, tags)
 *
 * Opens initially on Page 1 (Music Player).
 * Swiping right from Music Player reveals Page 0 (On Playing).
 * Swiping left from On Playing returns to Page 1 (Music Player).
 * Back from either page (system back or visible back control) returns to Home via [onBackClick].
 */
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    // System back returns to Home from either page without duplicate navigation
    BackHandler(onBack = onBackClick)

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 2 },
    )

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        when (page) {
            0 -> NowPlayingContent(
                songs = uiState.queue,
                currentQueueIndex = uiState.currentQueueIndex,
                onSongClick = { index -> viewModel.jumpToQueueItem(index) },
                onBackClick = onBackClick,
            )
            1 -> MusicPlayerContent(
                uiState = uiState,
                onBackClick = onBackClick,
                onPlayPause = viewModel::togglePlayPause,
                onPrevious = viewModel::previous,
                onNext = viewModel::next,
                onSeek = viewModel::seekTo,
                onShuffle = viewModel::toggleShuffle,
                onRepeatAll = viewModel::toggleRepeatAll,
                onOpenQueue = {
                    scope.launch { pagerState.animateScrollToPage(0) }
                },
            )
        }
    }
}
