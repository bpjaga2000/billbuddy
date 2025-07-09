package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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
            TextEdit(value, "0.00", keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
        }
    }
}