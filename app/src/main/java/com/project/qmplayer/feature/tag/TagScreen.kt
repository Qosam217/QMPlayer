package com.project.qmplayer.feature.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.project.qmplayer.core.ui.components.ConfirmDeleteDialog
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.feature.tag.components.CreateTagDialog
import com.project.qmplayer.feature.tag.components.TagCard
import com.project.qmplayer.feature.tag.components.TagInfoDialog
import com.project.qmplayer.feature.tag.components.TagOptionModal

@Composable
fun TagScreen(
    viewModel: TagViewModel,
    modifier: Modifier = Modifier,
    onTagClick: (Long) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()

    var expandedTagByIds by remember { mutableStateOf(emptySet<Long>()) }
    var selectedTagItem by remember { mutableStateOf<TagItem?>(null) }
    var tagToRename by remember { mutableStateOf<Tag?>(null) }
    var tagToDelete by remember { mutableStateOf<Tag?>(null) }
    var tagForInfo by remember { mutableStateOf<TagItem?>(null) }

    val existingNames = remember(uiState.tags) {
        uiState.tags.map { it.tag.name }
    }

    when{
        uiState.isLoading -> {
            // Keep existing UI structure
        }

        uiState.tags.isEmpty() -> {
            EmptyTagContent(modifier = modifier)
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection(
                    title = "Sort: ${sortOrder.label}",
                    onSortClick = { viewModel.toggleSortOrder() },
                    modifier = Modifier.fillMaxWidth()
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = uiState.tags,
                        key = { tag -> tag.tag.id }
                    ){ item ->
                        val tagId = item.tag.id

                        TagCard(
                            tag = item.tag,
                            songs = item.songs,
                            expanded = tagId in expandedTagByIds,
                            onExpandClick = {
                                if(tagId in expandedTagByIds){
                                    expandedTagByIds = expandedTagByIds - tagId
                                }else{
                                    expandedTagByIds = expandedTagByIds + tagId
                                }
                            },
                            onMenuClick = {
                                selectedTagItem = item
                            }
                        )
                    }
                }
            }
        }
    }

    selectedTagItem?.let { item ->
        TagOptionModal(
            onDismiss = {
                selectedTagItem = null
            },
            onOptionClick = { option ->
                when (option) {
                    TagOption.PLAY -> {
                        onTagClick(item.tag.id)
                    }
                    TagOption.RENAME -> {
                        tagToRename = item.tag
                    }
                    TagOption.DELETE -> {
                        tagToDelete = item.tag
                    }
                    TagOption.INFO -> {
                        tagForInfo = item
                    }
                }
                selectedTagItem = null
            }
        )
    }

    tagToRename?.let { tag ->
        CreateTagDialog(
            existingNames = existingNames,
            initialName = tag.name,
            title = "Rename Tag",
            confirmLabel = "Save",
            onConfirm = { newName ->
                viewModel.renameTag(tag, newName)
                tagToRename = null
            },
            onDismiss = { tagToRename = null }
        )
    }

    tagToDelete?.let { tag ->
        ConfirmDeleteDialog(
            title = "Delete Tag",
            message = "Are you sure you want to delete '${tag.name}'?",
            onConfirm = {
                viewModel.deleteTag(tag)
                tagToDelete = null
            },
            onDismiss = { tagToDelete = null }
        )
    }

    tagForInfo?.let { item ->
        TagInfoDialog(
            tag = item.tag,
            songs = item.songs,
            onDismiss = { tagForInfo = null }
        )
    }
}

@Composable
private fun EmptyTagContent(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No tags found")
    }
}
