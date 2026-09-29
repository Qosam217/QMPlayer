package com.project.qmplayer.data.media

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.project.qmplayer.domain.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class MediaStoreDataSourceImpl(
    context: Context
): MediaStoreDataSource {

    private val contentResolver: ContentResolver = context.applicationContext.contentResolver

    private val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    override fun observeSongs(): Flow<List<Song>> {
        return callbackFlow {
            fun querySongs(){
                trySend(querySongsFromMediaStore())
            }

            val observer = object : ContentObserver(
                Handler(Looper.getMainLooper())
            ){
                override fun onChange(selfChange: Boolean) {
                    querySongs()
                }

                override fun onChange(selfChange: Boolean, uri: Uri?) {
                    querySongs()
                }
            }

            contentResolver.registerContentObserver(
                collection,
                true,
                observer
            )

            querySongs()

            awaitClose {
                contentResolver.unregisterContentObserver(observer)
            }
        }.distinctUntilChanged().flowOn(Dispatchers.IO)
    }

    private fun querySongsFromMediaStore (): List<Song>{
        val songs = mutableListOf<Song>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.RELATIVE_PATH
        )

        val selection = """
            ${MediaStore.Audio.Media.IS_MUSIC} != 0
            AND ${MediaStore.Audio.Media.RELATIVE_PATH} = ?
        """.trimIndent()

        val selectionArgs = arrayOf(
            "Music/QMPlayer/"
        )

        val sortOrder = "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder
        ).use { cursor ->
            val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val durationColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            while (cursor!!.moveToNext()){
                val id = cursor.getLong(idColumn!!)
                val title = cursor.getString(titleColumn!!)
                val artist = cursor.getString(artistColumn!!)
                val album = cursor.getString(albumColumn!!)
                val albumId = cursor.getLong(albumIdColumn!!)
                val duration = cursor.getLong(durationColumn!!)

                val uri = ContentUris.withAppendedId(
                    collection,
                    id
                )
                val artworkUri = if(albumId > 0){
                    ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                        albumId
                    ).toString()
                }else{
                    null
                }

                songs += Song(
                    uri = uri.toString(),
                    title = title,
                    duration = duration,
                    artist = artist,
                    album = album,
                    artworkUri = artworkUri
                )
            }
        }
        return songs
    }

    private fun querySong(songUri: String): Song? {
        val uri = Uri.parse(songUri)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            if (!cursor.moveToFirst()) {
                return null
            }
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            val albumId = cursor.getLong(albumIdColumn)

            val artworkUri = if(albumId > 0) {
                ContentUris.withAppendedId(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    albumId
                ).toString()
            }else{
                null
            }
            return Song(
                uri = uri.toString(),
                title = cursor.getString(titleColumn),
                duration = cursor.getLong(durationColumn),
                artist = cursor.getString(artistColumn),
                album = cursor.getString(albumColumn),
                artworkUri = artworkUri
            )
        }
        return null
    }


    override suspend fun getSong(songUri: String): Song? {
        return querySong(songUri)
    }
}