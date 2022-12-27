package ui.widgets.util

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import util.Action

val LocalIsDarkMode = compositionLocalOf { mutableStateOf(false) }


suspend fun showInvalidValueSnackBar(scaffoldState: ScaffoldState, itemName: String) {
    scaffoldState.snackbarHostState.showSnackbar("$itemName value is invalid")
}

suspend fun showSuccess(scaffoldState: ScaffoldState, itemName: String, action: Action) {
    scaffoldState.snackbarHostState.showSnackbar(
        "$itemName ${action.pastTense} Successfully"
    )
}