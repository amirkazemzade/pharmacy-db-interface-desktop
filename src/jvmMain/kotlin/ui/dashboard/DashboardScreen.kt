package ui.dashboard

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun DashboardScreen() {
    val viewModel = remember { DashboardViewModel() }
    val countMedsState = viewModel.countMedsState.collectAsState().value

    val scope = rememberCoroutineScope()
    Row {
        Button(
            onClick = { countMeds(scope, viewModel) }
        ) {
            when (countMedsState) {
                CountMedsState.Initial -> Text("Count Meds")
                CountMedsState.Loading -> CircularProgressIndicator(color = Color.White)
                is CountMedsState.Success -> Text("There are ${countMedsState.medsCount} meds")
                is CountMedsState.Error -> Text(countMedsState.error, color = Color.Red)
            }
        }
    }
}

private fun countMeds(scope: CoroutineScope, viewModel: DashboardViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.countMeds() }
}