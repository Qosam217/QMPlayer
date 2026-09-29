package com.project.qmplayer.domain.repository

import com.project.qmplayer.domain.model.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    fun observeTags(): Flow<List<Tag>>

    suspend fun getTag(id: Long): Tag?

    suspend fun createTag(name: String): Long

    suspend fun updateTag(tag: Tag)

    suspend fun deleteTag(tag: Tag)

    fun observeTagsForSong(songUri: String): Flow<List<Tag>>

    fun observeSongsForTag(tagId: Long): Flow<List<String>>

    suspend fun addTagToSong(songUri: String, tagId: Long)

    suspend fun removeTagFromSong(songUri: String, tagId: Long)
}