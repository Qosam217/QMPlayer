package com.project.qmplayer.data.repository

import com.project.qmplayer.data.local.dao.SongTagDao
import com.project.qmplayer.data.local.dao.TagDao
import com.project.qmplayer.data.local.entity.SongTagCrossRef
import com.project.qmplayer.data.local.entity.TagEntity
import com.project.qmplayer.domain.model.Tag
import com.project.qmplayer.domain.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TagRepositoryImpl(
    private val tagDao: TagDao,
    private val songTagDao: SongTagDao
) : TagRepository {

    override fun observeTags(): Flow<List<Tag>> {
        return tagDao.observeAll()
            .map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTag(id: Long): Tag? {
        return tagDao.observeById(id)
            .firstOrNull()
            ?.toDomain()
    }

    override suspend fun createTag(name: String): Long {
        return tagDao.insert(TagEntity(
            name = name
        ))
    }

    override suspend fun updateTag(tag: Tag) {
        tagDao.update(TagEntity(
            id = tag.id,
            name = tag.name
        ))
    }

    override suspend fun deleteTag(tag: Tag) {
        songTagDao.deleteByTagId(tag.id)
        tagDao.delete(TagEntity(
            id = tag.id,
            name = tag.name
        ))
    }

    override fun observeTagsForSong(songUri: String): Flow<List<Tag>> {
        return songTagDao.observeTagsForSong(songUri)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun observeSongsForTag(tagId: Long): Flow<List<String>> {
        return songTagDao.observeSongsForTag(tagId)
            .map {
                refs -> refs.map { it.songUri }
            }
    }

    override suspend fun addTagToSong(songUri: String, tagId: Long) {
        songTagDao.insert(SongTagCrossRef(songUri, tagId))
    }

    override suspend fun removeTagFromSong(songUri: String, tagId: Long) {
        songTagDao.delete(SongTagCrossRef(songUri, tagId))
    }

    override suspend fun removeAllTagsFromSong(songUri: String) {
        songTagDao.deleteAllTagsForSong(songUri)
    }

    private fun TagEntity.toDomain(): Tag {
        return Tag(
            id = id,
            name = name
        )
    }
}