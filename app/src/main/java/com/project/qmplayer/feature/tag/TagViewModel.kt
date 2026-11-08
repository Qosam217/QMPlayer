package com.project.qmplayer.feature.tag

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class TagSortOrder(val label: String) {
    NAME_ASC("Name (A-Z)"),
    NAME_DESC("Name (Z-A)"),
    SONG_COUNT_DESC("Most Songs")
}

@OptIn(ExperimentalCoroutinesApi::class)
class TagViewModel (
    private val tagRepository: TagRepository,
    private val songRepository: SongRepository
): ViewModel() {

    private val _sortOrder = MutableStateFlow(TagSortOrder.NAME_ASC)
    val sortOrder: StateFlow<TagSortOrder> = _sortOrder.asStateFlow()

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

    private fun observeTagItems(tags: List<Tag>): Flow<List<TagItem>> {
        val flows = tags.map { observeTagItem(it) }
        return combine(flows){ items ->
            items.toList()
        }
    }

    val uiState: StateFlow<TagUiState> =
        combine(
            tagRepository.observeTags().flatMapLatest { tags ->
                if (tags.isEmpty()){
                    flowOf(emptyList())
                }else{
                    observeTagItems(tags)
                }
            },
            _sortOrder
        ) { tagItems, sortOrder ->
            val sorted = when (sortOrder) {
                TagSortOrder.NAME_ASC -> tagItems.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.tag.name })
                TagSortOrder.NAME_DESC -> tagItems.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.tag.name })
                TagSortOrder.SONG_COUNT_DESC -> tagItems.sortedByDescending { it.songs.size }
            }
            TagUiState(
                tags = sorted,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TagUiState()
        )

    fun toggleSortOrder() {
        val next = when (_sortOrder.value) {
            TagSortOrder.NAME_ASC -> TagSortOrder.NAME_DESC
            TagSortOrder.NAME_DESC -> TagSortOrder.SONG_COUNT_DESC
            TagSortOrder.SONG_COUNT_DESC -> TagSortOrder.NAME_ASC
        }
        _sortOrder.value = next
    }

    fun createTag(name: String){
        viewModelScope.launch {
            tagRepository.createTag(name)
        }
    }

    fun renameTag(tag: Tag, newName: String) {
        viewModelScope.launch {
            tagRepository.updateTag(tag.copy(name = newName))
        }
    }

    fun deleteTag(tag: Tag) {
        viewModelScope.launch {
            tagRepository.deleteTag(tag)
        }
    }
}
