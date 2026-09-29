package com.project.qmplayer.core.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun BottomNavigation(
    selected: NavigationPage,
    onSelect: (NavigationPage)-> Unit,
    modifier: Modifier = Modifier
){
    val pages = NavigationPage.entries
    val selectedIndex = pages.indexOf(selected)

    val indicatorPosition by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "navigationIndicator"
    )
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(50.dp)
            ),
        shape = RoundedCornerShape(50.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            val itemWidth = maxWidth / pages.size
            val itemWidthPx = with(LocalDensity.current) { itemWidth.toPx() }

            // Indicator
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .offset {
                        IntOffset(
                            x = (
                                    itemWidthPx * indicatorPosition
                                    ).roundToInt(),
                            y = 0
                        )
                    }
                    .clip(RoundedCornerShape(50.dp))
                    .background(
                        MaterialTheme.colorScheme.surfaceContainer
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(50.dp)
                    )
            )

            // Items
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                pages.forEach { page ->
                    BottomNavigationItem(
                        tab = page,
                        isSelected = page == selected,
                        onClick = {
                            onSelect(page)
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            /*Row(
            //modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 0.dp, bottom = 0.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            NavigationPage.entries.forEach {
                BottomNavigationItem(
                    tab = it,
                    isSelected = it == selected,
                    onClick = { onSelect(it) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
         */
        }
    }
}