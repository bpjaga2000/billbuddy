package presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.SpendWithSplit
import io.ktor.util.date.GMTDate

@Composable
fun SpendListItem(
    spendWithSplit: SpendWithSplit,
    onSpendClick: () -> Unit,
    spentBy: Map<String, String>,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.then(Modifier.padding(vertical = 8.dp).fillMaxWidth()),
        onClick = onSpendClick
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = GMTDate(spendWithSplit.spend.updatedAt).month.value,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = GMTDate(spendWithSplit.spend.updatedAt).dayOfMonth.toString(),
                    modifier = Modifier.padding(5.dp)
                )
            }
            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = spendWithSplit.spend.name, modifier = Modifier.padding(5.dp))
                if (spendWithSplit.owe == 0.0)
                    Text("You are not involved", modifier = Modifier.padding(5.dp))
                else
                    Text(
                        text = "${spentBy.values.joinToString(", ")} paid ${spendWithSplit.spend.totalAmount}",
                        modifier = Modifier.padding(5.dp)
                    )
            }
            Column(
                modifier = Modifier.weight(0.2f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                if (spendWithSplit.owe == 0.0)
                    Text(text = "not involved", modifier = Modifier.padding(5.dp))
                else if (spendWithSplit.owe > 0) {
                    Text(text = "you owe", modifier = Modifier.padding(5.dp))
                    Text(text = spendWithSplit.owe.toString(), modifier = Modifier.padding(5.dp))
                } else {
                    Text(text = "you get", modifier = Modifier.padding(5.dp))
                    Text(
                        text = (spendWithSplit.owe * -1.0).toString(),
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}