package presentation.spenddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SpendDetailsScreen(
    spendDetailsComponent: SpendDetailsComponent,
    modifier: Modifier = Modifier,
) {

    Column {
        Text("Spend name")
        Text("Amount")
        Text("Added by abc on 123")
        for (i in 1..2) {
            Text("$i paid ${i * 100} and owes ${i * 10}")
        }
        for (i in 1..8) {
            Text("${i + 2} owes ${i * 10}")
        }
    }

}