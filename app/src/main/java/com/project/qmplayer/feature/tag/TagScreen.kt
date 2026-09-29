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
import com.project.qmplayer.core.ui.components.SortSection
import com.project.qmplayer.feature.tag.components.TagCard

@Composable
fun TagScreen(
    viewModel: TagViewModel,
    modifier: Modifier = Modifier,
    onTagClick: (Long) -> Unit,
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var expandedTagByIds by remember {
        mutableStateOf(emptySet<Long>())
    }

    when{
        uiState.isLoading -> {

        }

        uiState.tags.isEmpty() -> {
            EmptyTagContent(modifier = modifier)
        }

        else -> {
            Column(modifier = modifier.fillMaxSize()) {
                SortSection("Sort", {}, modifier = Modifier.fillMaxWidth())

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
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
                                onTagClick(tagId)
                            }

                        )
                    }
                }
            }
        }
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
        Text("No Tags Found")
    }
}