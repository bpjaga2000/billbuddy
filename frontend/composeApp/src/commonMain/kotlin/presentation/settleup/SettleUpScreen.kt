package presentation.settleup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import presentation.common.TextEdit

@Composable
fun SettleUpScreen(component: SettleUpComponent, modifier: Modifier = Modifier) {

    val amount = mutableStateOf("")

    Box {
        Column {
            Text("${component.borrower.name} paid ${component.lender.name}")

            TextEdit(amount, "0.00")
        }
        IconButton(onClick = component::onSettleClicked) {
            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "settle")
        }
    }

}