package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import constants.SplitType.AMOUNT
import constants.SplitType.DIFFERENCE
import constants.SplitType.EQUAL
import constants.SplitType.RATIO
import constants.SplitType.SHARE
import data.model.EditSpendTabDetails

@Composable
fun EditSpendTabItem(
    editDetails: EditSpendTabDetails,
    modifier: Modifier = Modifier,
) {
    editDetails.apply {

        Row(modifier = modifier) {

            if (editDetails.type == EQUAL)
                Checkbox(
                    value.value != "",
                    onChecked
                )

            Text(name)

            if (editDetails.type == AMOUNT)
                Text("₹")

            if (editDetails.type != EQUAL)
                TextEdit(
                    value,
                    when (type) {
                        AMOUNT -> "0.00"
                        SHARE -> "0"
                        RATIO -> "0.00"
                        DIFFERENCE -> "0.00"
                        else -> ""
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (type) {
                            SHARE -> KeyboardType.Number
                            else -> KeyboardType.Decimal
                        }
                    )
                )

        }

    }
}