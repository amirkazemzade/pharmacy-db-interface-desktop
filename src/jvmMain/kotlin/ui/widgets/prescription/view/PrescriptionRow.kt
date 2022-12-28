package ui.widgets.prescription.view

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
import data.model.Prescription
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell
import util.*


@Composable
fun PrescriptionRow(prescription: Prescription, onDelete: (prescription: Prescription) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${prescription.id}") }
        TableCell(weight = 2F) { Text("${prescription.date}") }
        TableCell(weight = 2F) { Text("${prescription.totalPrice}") }
        TableCell(weight = 2F) { Text("${prescription.isPaid}") }
        TableCell(weight = 2F) { Text("${prescription.doctorId}") }
        TableCell(weight = 2F) { Text("${prescription.patientId}") }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditPrescriptionScreen(prescription) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(prescription) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun PrescriptionTitleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text(idText) }
        TableCell(weight = 2F) { Text(dateText) }
        TableCell(weight = 2F) { Text(totalPriceText) }
        TableCell(weight = 2F) { Text(isPaidText) }
        TableCell(weight = 2F) { Text(doctorIdText) }
        TableCell(weight = 2F) { Text(patientIdText) }
        TableCell(weight = 2F) { Text(actionsText) }
    }
}
