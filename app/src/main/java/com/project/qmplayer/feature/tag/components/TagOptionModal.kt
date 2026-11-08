package com.project.qmplayer.feature.tag.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.project.qmplayer.feature.tag.TagOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagOptionModal(
    onDismiss: () -> Unit,
    onOptionClick: (TagOption) -> Unit,
    modifier: Modifier = Modifier
){
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            TagOptionItem(
                icon = { Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null) },
                title = "Play",
                onClick = {
                    onOptionClick(TagOption.PLAY)
                }
            )

            TagOptionItem(
                icon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null) },
                title = "Rename",
                onClick = {
                    onOptionClick(TagOption.RENAME)
                }
            )

            TagOptionItem(
                icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                title = "Delete",
                onClick = {
                    onOptionClick(TagOption.DELETE)
                }
            )

            TagOptionItem(
                icon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
                title = "Info",
                onClick = {
                    onOptionClick(TagOption.INFO)
                }
            )
        }
    }
}

@Composable
fun TagOptionItem(
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
