package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import data.model.EditSpendDetails

@Composable
fun EditSpendItem(
    editDetails: EditSpendDetails,
    modifier: Modifier = Modifier,
) {
    editDetails.apply {
        Row(modifier = modifier) {
            Text(name)
            Text("₹")
            TextEdit(value, "0.00")
        }
    }
}