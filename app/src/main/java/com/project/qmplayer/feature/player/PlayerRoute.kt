package com.project.qmplayer.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.qmplayer.di.QMPlayerContainer

@Composable
fun PlayerRoute(
    container: QMPlayerContainer,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModel.Factory(container)
    )

    PlayerScreen(
        viewModel = viewModel,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}
