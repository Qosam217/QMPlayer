package com.project.qmplayer.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Tag
import androidx.compose.ui.graphics.vector.ImageVector
import com.project.qmplayer.R

enum class NavigationPage(
    val icon: ImageVector,
    val label: String,
    val headerIcon: Int
) {
    Library(
        Icons.Filled.LibraryMusic,
        "Library",
        R.drawable.setting_icon),
    Queue(
        Icons.AutoMirrored.Filled.QueueMusic,
        "Playlists",
        R.drawable.add_icon),
    Discover(
        Icons.Filled.Tag,
        "Tags",
        R.drawable.add_icon)
}