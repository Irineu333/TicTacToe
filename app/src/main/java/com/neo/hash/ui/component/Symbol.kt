package com.neo.hash.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.neo.hash.domain.model.Hash

@Composable
fun Symbol(
    symbol: Hash.Symbol,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary
) = Icon(
    imageVector = when (symbol) {
        Hash.Symbol.O -> Icons.Outlined.Circle
        Hash.Symbol.X -> Icons.Outlined.Close
    },
    tint = color,
    contentDescription = null,
    modifier = modifier
)
