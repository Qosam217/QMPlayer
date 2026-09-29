package com.project.qmplayer.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.project.qmplayer.R

@Composable
fun HomeHeader(
    navigation: NavigationPage,
    onSearchClick: () -> Unit,
    onActionClick: () -> Unit
){
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(52.dp).padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = navigation.label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        IconButton(
            onClick = onSearchClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                modifier = Modifier.size(28.dp),
                contentDescription = "Search Button"
            )
        }

        IconButton(
            onClick = onActionClick
        ) {
            Icon(
                painter = painterResource(id = navigation.headerIcon),
                modifier = Modifier.size(28.dp),
                contentDescription = "Setting Button"
            )
        }
    }
}