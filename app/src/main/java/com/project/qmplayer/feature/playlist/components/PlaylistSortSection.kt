package com.project.qmplayer.feature.playlist.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistSortSection(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text("Sort")
    }
}