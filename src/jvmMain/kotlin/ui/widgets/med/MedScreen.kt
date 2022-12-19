package ui.widgets.med

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MedScreen() {
    val viewModel = remember { MedViewModel() }
    val medsListState = viewModel.medsListState.collectAsState().value
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // TODO insert item
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Insert Item")
            }
        },
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (medsListState) {
                MedsListState.Initial -> getMeds(scope, viewModel)
                MedsListState.Loading -> CircularProgressIndicator()
                is MedsListState.Success -> MedsList(medsListState)
                is MedsListState.Error -> Text(medsListState.error, color = Color.Red)
            }
        }
    }
}

@Composable
private fun MedsList(medsListState: MedsListState.Success) {
    Box(modifier = Modifier.fillMaxSize()) {
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            item {
                MedTitleRow()
            }
            items(medsListState.meds) { med ->
                MedRow(med)
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = state
            )
        )
    }
}

private fun getMeds(scope: CoroutineScope, viewModel: MedViewModel) {
    scope.launch(Dispatchers.IO) { viewModel.getMeds() }
}

