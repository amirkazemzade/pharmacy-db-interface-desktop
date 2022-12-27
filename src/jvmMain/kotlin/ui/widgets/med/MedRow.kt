package ui.widgets.med

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.model.Med
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell


@Composable
fun MedRow(med: Med, onDelete: (med: Med) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${med.id}") }
        TableCell(weight = 1F) { Text("${med.pharmId}") }
        TableCell(weight = 1F) { Text("${med.compId}") }
        TableCell(weight = 1F) { Text("${med.inv}") }
        TableCell(weight = 1F) { Text("${med.price}") }
        TableCell(weight = 2F) { Text("${med.expirationDate}") }
        TableCell(weight = 3F) { Text(med.medName ?: "") }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditMedScreen(med) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(med) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun MedTitleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("id") }
        TableCell(weight = 1F) { Text("pharm id") }
        TableCell(weight = 1F) { Text("comp id") }
        TableCell(weight = 1F) { Text("inv") }
        TableCell(weight = 1F) { Text("price") }
        TableCell(weight = 2F) { Text("expiration date") }
        TableCell(weight = 3F) { Text("med name") }
        TableCell(weight = 2F) { Text("Actions") }
    }
}
