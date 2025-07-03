package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import constants.SplitType.AMOUNT
import constants.SplitType.DIFFERENCE
import constants.SplitType.EQUAL
import constants.SplitType.RATIO
import constants.SplitType.SHARE
import data.model.EditSpendTabDetails

@Composable
fun EditSpendTabItem(
    editDetails: EditSpendTabDetails,
    type: Int,
    modifier: Modifier = Modifier,
) {
    editDetails.apply {

        Row(modifier = modifier) {

            if (type == EQUAL)
                Checkbox(
                    value.value != "",
                    onChecked
                )

            Text(name)

            if (type == AMOUNT)
                Text("₹")

            if (type != EQUAL)
                TextEdit(
                    value,
                    when (type) {
                        AMOUNT -> "0.00"
                        SHARE -> "0"
                        RATIO -> "0.00"
                        DIFFERENCE -> "0.00"
                        else -> ""
                    }
                )

        }

    }
}