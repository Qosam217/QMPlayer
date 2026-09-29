package com.project.qmplayer.feature.playlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.qmplayer.domain.model.Song

@Composable
fun PlaylistSongList(
    songs: List<Song>
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 12.dp
            )
    ) {
        songs.forEach { song ->
            Text(
                text = song.title,
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

}