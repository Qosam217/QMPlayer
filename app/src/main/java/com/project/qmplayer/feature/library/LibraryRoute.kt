package com.project.qmplayer.feature.library

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.qmplayer.di.QMPlayerContainer
import com.project.qmplayer.domain.model.Song

@Composable
fun LibraryRoute(
    container: QMPlayerContainer,
    onSongClick: (String) -> Unit,
    onSongMenuClick: (Song) -> Unit,
    modifier: Modifier = Modifier
){
    val viewModel: LibraryViewModel = viewModel(
        factory = LibraryViewModelFactory(
            container.songRepository
        )
    )

    LibraryScreen(
        viewModel = viewModel,
        onSongClick = onSongClick,
        onSongMenuClick = onSongMenuClick,
        modifier = modifier
    )
}