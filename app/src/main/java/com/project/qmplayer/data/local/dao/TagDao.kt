package com.project.qmplayer.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.project.qmplayer.data.local.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    @Query("SELECT * FROM tags")
    fun observeAll(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE id = :id")
    fun observeById(id: Long): Flow<TagEntity>

    @Insert
    suspend fun insert(tagEntity: TagEntity)

    @Update
    suspend fun update(tagEntity: TagEntity)

    @Delete
    suspend fun delete(tagEntity: TagEntity)
}