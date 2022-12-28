package ui.widgets.pharm.view

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
import data.model.Pharm
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell
import util.*


@Composable
fun PharmRow(pharm: Pharm, onDelete: (pharm: Pharm) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${pharm.id}") }
        TableCell(weight = 1F) { Text(pharm.name) }
        TableCell(weight = 1F) { Text("${pharm.needPrescription}") }
        TableCell(weight = 3F) { Text(pharm.usage ?: "") }
        TableCell(weight = 3F) { Text(pharm.sideEffects ?: "") }
        TableCell(weight = 1F) { Text("${pharm.categoryId}") }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditPharmScreen(pharm) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(pharm) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun PharmTitleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text(idText) }
        TableCell(weight = 1F) { Text(nameText) }
        TableCell(weight = 1F) { Text(needPrcText) }
        TableCell(weight = 3F) { Text(usageText) }
        TableCell(weight = 3F) { Text(sideEffectsText) }
        TableCell(weight = 1F) { Text(categoryIdText) }
        TableCell(weight = 2F) { Text(actionsText) }
    }
}
