package com.project.qmplayer.di

import android.content.Context
import com.project.qmplayer.data.local.database.QMPlayerDatabase
import com.project.qmplayer.data.media.MediaStoreDataSource
import com.project.qmplayer.data.media.MediaStoreDataSourceImpl
import com.project.qmplayer.data.repository.PlaylistRepositoryImpl
import com.project.qmplayer.data.repository.SongMetaDataRepositoryImpl
import com.project.qmplayer.data.repository.SongRepositoryImpl
import com.project.qmplayer.data.repository.TagRepositoryImpl
import com.project.qmplayer.domain.repository.PlaylistRepository
import com.project.qmplayer.domain.repository.SongMetaDataRepository
import com.project.qmplayer.domain.repository.SongRepository
import com.project.qmplayer.domain.repository.TagRepository

class QMPlayerContainer(context: Context) {

    private val applicationContext = context.applicationContext

    val database: QMPlayerDatabase by lazy {
        QMPlayerDatabase.getInstance(applicationContext)
    }

    val songMetadataRepository: SongMetaDataRepository by lazy {
        SongMetaDataRepositoryImpl(
            database.SongMetaDataDao()
        )
    }

    val tagRepository: TagRepository by lazy {
        TagRepositoryImpl(
            database.tagDao(),
            database.songTagDao()
        )
    }

    val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepositoryImpl(
            database.playlistDao(),
            database.playlistSongDao()
        )
    }

    val mediaStoreDataSource: MediaStoreDataSource by lazy {
        MediaStoreDataSourceImpl(applicationContext)
    }

    val songRepository: SongRepository by lazy {
        SongRepositoryImpl(mediaStoreDataSource)
    }
}