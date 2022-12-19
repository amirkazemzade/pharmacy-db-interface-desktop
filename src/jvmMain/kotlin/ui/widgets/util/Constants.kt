package ui.widgets.util

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

val LocalIsDarkMode = compositionLocalOf { mutableStateOf(false) }