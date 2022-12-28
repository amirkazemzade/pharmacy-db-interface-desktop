package ui.widgets.doctor.view

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
import data.model.Doctor
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell
import util.*


@Composable
fun DoctorRow(doctor: Doctor, onDelete: (doctor: Doctor) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${doctor.id}") }
        TableCell(weight = 2F) { Text(doctor.firstName) }
        TableCell(weight = 2F) { Text(doctor.lastName) }
        TableCell(weight = 3F) { Text(doctor.email ?: "") }
        TableCell(weight = 3F) { Text(doctor.phone ?: "") }
        TableCell(weight = 4F) { Text(doctor.address ?: "") }
        TableCell(weight = 2F) { Text(doctor.licenceId) }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditDoctorScreen(doctor) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(doctor) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun DoctorTitleRow() {
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
        TableCell(weight = 4F) { Text(birthDateText) }
        TableCell(weight = 2F) { Text(insuranceIdText) }
        TableCell(weight = 2F) { Text(actionsText) }
    }
}
