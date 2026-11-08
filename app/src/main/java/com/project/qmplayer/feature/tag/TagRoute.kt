package com.project.qmplayer.feature.tag

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.qmplayer.di.QMPlayerContainer
import com.project.qmplayer.feature.tag.components.CreateTagDialog

@Composable
fun TagRoute(
    container: QMPlayerContainer,
    modifier: Modifier = Modifier,
    showCreateDialog: Boolean,
    onCreateDialogDismiss: () -> Unit,
    onTagClick: (Long) -> Unit
) {
    val tagViewModel: TagViewModel = viewModel(
        factory = TagViewModelFactory(
            container.tagRepository,
            container.songRepository
        )
    )

    TagScreen(
        viewModel = tagViewModel,
        onTagClick = onTagClick,
        modifier = modifier
    )

    if(showCreateDialog){
        val existingNames = tagViewModel.uiState.collectAsStateWithLifecycle().value.tags.map { it.tag.name }
        CreateTagDialog(
            existingNames = existingNames,
            onConfirm = { name ->
                tagViewModel.createTag(name)
                onCreateDialogDismiss()
            },
            onDismiss = onCreateDialogDismiss
        )
    }
}