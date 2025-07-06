package presentation.settleup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.common.TextEdit

@Composable
fun SettleUpScreen(component: SettleUpComponent, modifier: Modifier = Modifier) {

    val payerName by remember { component.payerName }
    val payeeName by remember { component.payeeName }

    Column(modifier = modifier.wrapContentHeight()) {
        if (component.amount < 0)
            Text("$payerName pays $payeeName")
        else
            Text("$payeeName pays $payerName")
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextEdit(component.amountToPay, "0.00", editable = false)
            IconButton(onClick = component::onSettleClicked) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "settle",
                    Modifier.size(32.dp)
                )
            }
        }
    }

}