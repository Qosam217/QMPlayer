package com.project.qmplayer.feature.playlist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.qmplayer.di.QMPlayerContainer
import com.project.qmplayer.feature.playlist.components.CreatePlaylistDialog

@Composable
fun PlaylistRoute(
    container: QMPlayerContainer,
    modifier: Modifier = Modifier,
    showCreateDialog: Boolean,
    onCreateDialogDismiss: () -> Unit,
    onPlaylistClick: (Long) -> Unit,
){
    val viewModel: PlaylistViewModel = viewModel(
        factory = PlaylistViewModelFactory(
            playlistRepository = container.playlistRepository,
            songRepository = container.songRepository
        )
    )

    PlaylistScreen(
        viewModel = viewModel,
        modifier = modifier,
        onPlaylistClick = onPlaylistClick
    )

    if(showCreateDialog){
        CreatePlaylistDialog(
            onConfirm = { name ->
                viewModel.createPlaylist(name)
                onCreateDialogDismiss()
            },
            onDismiss = onCreateDialogDismiss
        )
    }
}