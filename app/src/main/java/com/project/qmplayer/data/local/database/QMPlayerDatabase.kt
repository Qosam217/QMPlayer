package com.project.qmplayer.data.local.database

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.project.qmplayer.data.local.dao.PlaylistDao
import com.project.qmplayer.data.local.dao.PlaylistSongDao
import com.project.qmplayer.data.local.dao.SongMetaDataDao
import com.project.qmplayer.data.local.dao.SongTagDao
import com.project.qmplayer.data.local.dao.TagDao
import com.project.qmplayer.data.local.entity.PlaylistEntity
import com.project.qmplayer.data.local.entity.PlaylistSongEntity
import com.project.qmplayer.data.local.entity.SongMetaDataEntity
import com.project.qmplayer.data.local.entity.SongTagCrossRef
import com.project.qmplayer.data.local.entity.TagEntity

@Database(
    entities = [
        PlaylistEntity::class,
        PlaylistSongEntity::class,
        TagEntity::class,
        SongTagCrossRef::class,
        SongMetaDataEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class QMPlayerDatabase: RoomDatabase() {
    abstract fun SongMetaDataDao(): SongMetaDataDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistSongDao(): PlaylistSongDao
    abstract fun tagDao(): TagDao
    abstract fun songTagDao(): SongTagDao

    companion object {
        @Volatile
        private var INSTANCE: QMPlayerDatabase? = null

        fun getInstance(context: Context): QMPlayerDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    QMPlayerDatabase::class.java,
                    "qmplayer.db"
                )
                    .setDriver(AndroidSQLiteDriver())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}