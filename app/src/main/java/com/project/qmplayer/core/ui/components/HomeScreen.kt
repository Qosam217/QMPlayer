package com.project.qmplayer.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.project.qmplayer.R
import com.project.qmplayer.di.QMPlayerContainer
import com.project.qmplayer.feature.library.LibraryRoute
import com.project.qmplayer.feature.player.PlayerRoute
import com.project.qmplayer.feature.playlist.PlaylistRoute
import com.project.qmplayer.feature.tag.TagRoute

@Composable
fun HomeScreen(
    container: QMPlayerContainer
){
    var tab by remember { mutableStateOf(NavigationPage.Library) }
    var playing by remember { mutableStateOf(true) }

    var showAddPlaylistDialog by remember { mutableStateOf(false) }
    var showAddTagDialog by remember { mutableStateOf(false) }

    var showPlayer by remember { mutableStateOf(false) }
    if(showPlayer){
        PlayerRoute(onBackClick = { showPlayer = false })
    }else{
        Scaffold(
            topBar = {
                HomeHeader(
                    navigation = tab,
                    onSearchClick = {
                        println("Search clicked")
                    },
                    onActionClick = {
                        when (tab) {
                            NavigationPage.Library -> {
                                println("Settings clicked")
                            }

                            NavigationPage.Queue -> {
                                showAddPlaylistDialog = true
                            }

                            NavigationPage.Discover -> {
                                showAddTagDialog = true
                            }
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                HomeBottom(
                    track = NowPlaying("Retro Velocity", "Neon Shard", playing),
                    artwork = painterResource(R.drawable.library_icon),
                    selectedTab = tab,
                    onSelectTab = { tab = it },
                    onPlayPause = { playing = !playing },
                    onPrevious = { },
                    onNext = { },
                )
            },
        ) { innerPadding ->
            when (tab){
                NavigationPage.Library -> LibraryRoute(
                    container = container,
                    onSongClick = { uri ->
                        println("Song clicked: $uri")
                    },
                    onSongMenuClick = { song ->
                        println("Song menu clicked: ${song.title}")
                    },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
                NavigationPage.Queue -> PlaylistRoute(
                    container = container,
                    onPlaylistClick = { id ->
                        println("Playlist clicked: $id")
                    },
                    showCreateDialog = showAddPlaylistDialog,
                    onCreateDialogDismiss = { showAddPlaylistDialog = false },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
                NavigationPage.Discover -> TagRoute(
                    container = container,
                    onTagClick = { id ->
                        println("Tag clicked: $id")
                    },
                    showCreateDialog = showAddTagDialog,
                    onCreateDialogDismiss = { showAddTagDialog = false },
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                )
            }
        }
    }
}