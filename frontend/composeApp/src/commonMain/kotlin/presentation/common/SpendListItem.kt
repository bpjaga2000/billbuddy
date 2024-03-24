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
import io.ktor.util.date.GMTDate

@Composable
fun SpendListItem(modifier: Modifier = Modifier/*, spend: Spends*/, involved: Boolean = false) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.then(Modifier.padding(15.dp).fillMaxWidth())
    ) {
        Row(
            modifier = Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Column(
                modifier = Modifier.weight(0.1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = GMTDate(/*spend.updatedAt*/).month.value)
                Text(text = GMTDate(/*spend.updatedAt*/).dayOfMonth.toString())
            }
            Column(
                modifier = Modifier.weight(0.6f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = "title")
                if (involved)
                    Text(text = "mr.a paid 200")
                else
                    Text("You are not involved")
            }
            Column(
                modifier = Modifier.weight(0.2f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (involved) {
                    Text(text = "your share")
                    Text(text = "200")
                } else
                    Text(text = "not involved")
            }
        }
    }
}