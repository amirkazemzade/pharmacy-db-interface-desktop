package ui.widgets.util

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.TableCell(weight: Float, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .weight(weight)
            .border(1.dp, MaterialTheme.colors.secondaryVariant)
            .padding(8.dp)
            .fillMaxHeight()
    ) {
        content()
    }
}

