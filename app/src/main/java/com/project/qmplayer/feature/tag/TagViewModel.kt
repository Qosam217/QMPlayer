package com.project.qmplayer.feature.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TagViewModel (
    private val tagRepository: TagRepository,
    private val songRepository: SongRepository
): ViewModel() {
    val uiState: StateFlow<TagUiState> =
        tagRepository
            .observeTags()
            .flatMapLatest { tags ->
                if (tags.isEmpty()){
                    flowOf(emptyList())
                }else{
                    observeTagItems(tags)
                }
            }
            .map { tagItems ->
                TagUiState(
                    tags = tagItems,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TagUiState()
            )

    private fun observeTagItems(tags: List<Tag>): Flow<List<TagItem>> {
        val flows = tags.map { observeTagItem(it) }
        return combine(flows){
            items -> items.toList()
        }
    }

    private fun observeTagItem(tag: Tag): Flow<TagItem> {
        return combine(
            tagRepository.observeSongsForTag(tag.id),
            songRepository.observeSongs()
        ){ songUris, songs ->
            val songsByUri = songs.associateBy { it.uri }
            val tagSongs = songUris.mapNotNull { uri -> songsByUri[uri] }
            TagItem(tag, tagSongs)
        }
    }

    fun createTag(name: String){
        viewModelScope.launch {
            tagRepository.createTag(name)
        }
    }
}
