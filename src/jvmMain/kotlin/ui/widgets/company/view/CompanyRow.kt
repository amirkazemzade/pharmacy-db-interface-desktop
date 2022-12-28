package ui.widgets.company.view

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
import data.model.Company
import ui.navigator.LocalNavigator
import ui.widgets.util.TableCell
import util.*


@Composable
fun CompanyRow(company: Company, onDelete: (company: Company) -> Unit) {
    val navigator = LocalNavigator.current

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text("${company.id}") }
        TableCell(weight = 2F) { Text(company.name) }
        TableCell(weight = 2F) { Text(company.country) }
        TableCell(weight = 2F) { Text(company.email) }
        TableCell(weight = 3F) { Text(company.address ?: "") }
        TableCell(weight = 2F) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navigator.invoke { EditCompanyScreen(company) }
                    }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit Item", tint = MaterialTheme.colors.secondary)
                }
                IconButton(
                    onClick = { onDelete(company) }
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete Item", tint = MaterialTheme.colors.secondary)
                }
            }
        }
    }
}

@Composable
fun CompanyTitleRow() {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primaryVariant)
            .height(intrinsicSize = IntrinsicSize.Max),
    ) {
        TableCell(weight = 1F) { Text(idText) }
        TableCell(weight = 2F) { Text(nameText) }
        TableCell(weight = 2F) { Text(countryText) }
        TableCell(weight = 2F) { Text(emailText) }
        TableCell(weight = 3F) { Text(addressText) }
        TableCell(weight = 2F) { Text(actionsText) }
    }
}
