package com.project.qmplayer.feature.playlist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.qmplayer.domain.model.Playlist
import com.project.qmplayer.domain.model.Song

@Composable
fun PlaylistCard(
    playlist: Playlist,
    songs: List<Song>,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = playlist.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${songs.size} songs",
                        maxLines = 1,
                    )
                }

                IconButton(onClick = onExpandClick) {
                    Icon(
                        imageVector = if (expanded){
                            Icons.Default.ExpandLess
                        }else{
                            Icons.Default.ExpandMore
                        },
                        contentDescription = if (expanded){
                            "Collapse playlist"
                        }else{
                            "Expand playlist"
                        }
                    )
                }

                IconButton(onClick = onMenuClick){
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }
            if (expanded){
                PlaylistSongList(
                    songs = songs,
                )
            }
        }
    }
}