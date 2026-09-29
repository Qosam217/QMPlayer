package com.project.qmplayer.feature.library.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.qmplayer.feature.library.SongOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongOptionModal(
    onDismiss: () -> Unit,
    onOptionClick: (SongOption) -> Unit,
    modifier: Modifier = Modifier
){
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null) },
                title = "Play",
                onClick = {
                    onOptionClick(SongOption.PLAY)
                }
            )

            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.LibraryAdd, contentDescription = null) },
                title = "Add to Playlist",
                onClick = {
                    onOptionClick(SongOption.ADD_TO_PLAYLIST)
                }
            )

            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.NewLabel, contentDescription = null) },
                title = "Add Tag",
                onClick = {
                    onOptionClick(SongOption.ADD_TAG)
                }
            )

            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
                title = "Rename",
                onClick = {
                    onOptionClick(SongOption.RENAME)
                }
            )

            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                title = "Delete",
                onClick = {
                    onOptionClick(SongOption.DELETE)
                }
            )

            SongOptionItem(
                icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                title = "Info",
                onClick = {
                    onOptionClick(SongOption.INFO)
                }
            )

        }
    }
}

@Composable
fun SongOption(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
){
    ListItem(
        headlineContent = { Text(text = title) },
        leadingContent = icon,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun SongOptionItem(
    icon: @Composable () -> Unit,
    title: String,
    onClick: () -> Unit
){
    ListItem(
        headlineContent = { Text(text = title) },
        leadingContent = icon,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
    )
}