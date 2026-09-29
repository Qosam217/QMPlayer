package com.project.qmplayer.data.local.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.project.qmplayer.data.local.entity.SongTagCrossRef
import com.project.qmplayer.data.local.entity.TagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongTagDao {

    @Query("""
        SELECT * FROM song_tag_cross_ref
        WHERE tagId = :tagId
    """)
    fun observeSongsForTag(tagId: Long): Flow<List<SongTagCrossRef>>

    @Insert
    suspend fun insert(songTagCrossRef: SongTagCrossRef)

    @Update
    suspend fun update(songTagCrossRef: SongTagCrossRef)

    @Delete
    suspend fun delete(songTagCrossRef: SongTagCrossRef)

    @Query("""
        DELETE FROM song_tag_cross_ref
        WHERE songUri = :songUri
        AND tagId = :tagId
    """)
    suspend fun deleteBySongUri(songUri: String, tagId: Long)

    @Query("""
        SELECT tags.* FROM tags
        INNER JOIN song_tag_cross_ref 
            ON tags.id = song_tag_cross_ref.tagId
        WHERE song_tag_cross_ref.songUri = :songUri
        ORDER BY tags.name ASC
    """)
    fun observeTagsForSong(songUri: String): Flow<List<TagEntity>>
}