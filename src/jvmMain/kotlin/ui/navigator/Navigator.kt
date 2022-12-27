package ui.navigator

import androidx.compose.runtime.*


val LocalNavigator = compositionLocalOf { { _: @Composable () -> Unit -> } }

@Composable
fun NavHost(initialScreen: @Composable () -> Unit) {
    val currentScreen = remember { mutableStateOf(initialScreen) }

    val setScreen = { screen: @Composable () -> Unit ->
        currentScreen.value = screen
    }


    CompositionLocalProvider(
        LocalNavigator provides setScreen
    ) {
        currentScreen.value()
    }
}