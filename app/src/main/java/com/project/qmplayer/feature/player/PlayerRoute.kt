package com.project.qmplayer.feature.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlayerRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel: PlayerViewModel = viewModel()

    PlayerScreen(
        viewModel = viewModel,
        onBackClick = onBackClick,
        modifier = modifier
    )
}