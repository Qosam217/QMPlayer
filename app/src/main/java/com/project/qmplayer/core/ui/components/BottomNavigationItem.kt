package com.project.qmplayer.core.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationItem(
    tab: NavigationPage,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "itemPressScale"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(50.dp))
            //.background(if (isSelected) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.surface)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = interactionSource
            )
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .graphicsLayer{
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ){
        Icon(
            imageVector = tab.icon,
            contentDescription = tab.label,
            tint = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp)
        )
    }
}