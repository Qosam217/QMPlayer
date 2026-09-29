package com.project.qmplayer.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun HomeBottom(
    track: NowPlaying,
    artwork: Painter,
    selectedTab: NavigationPage,
    onSelectTab: (NavigationPage) -> Unit,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MiniPlayer(
            track = track,
            artwork = artwork,
            onPlayPause = onPlayPause,
            onPrevious = onPrevious,
            onNext = onNext,
        )
        BottomNavigation(
            selected = selectedTab,
            onSelect = onSelectTab
        )
    }
}