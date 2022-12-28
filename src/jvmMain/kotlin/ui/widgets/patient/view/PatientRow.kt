package ui.widgets.patient.view

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
import data.model.Patient
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell
import util.*


@Composable
fun PatientRow(patient: Patient, onDelete: (patient: Patient) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${patient.id}") }
        TableCell(weight = 2F) { Text(patient.firstName) }
        TableCell(weight = 2F) { Text(patient.lastName) }
        TableCell(weight = 3F) { Text(patient.nationalNumber) }
        TableCell(weight = 3F) { Text(patient.phone ?: "") }
        TableCell(weight = 2F) { Text("${patient.birthDate}") }
        TableCell(weight = 2F) { Text("${patient.insuranceId}") }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditPatientScreen(patient) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(patient) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun PatientTitleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text(idText) }
        TableCell(weight = 2F) { Text(firstNameText) }
        TableCell(weight = 2F) { Text(lastNameText) }
        TableCell(weight = 3F) { Text(nationalNumberText) }
        TableCell(weight = 3F) { Text(phoneText) }
        TableCell(weight = 2F) { Text(birthDateText) }
        TableCell(weight = 2F) { Text(insuranceIdText) }
        TableCell(weight = 2F) { Text(actionsText) }
    }
}
