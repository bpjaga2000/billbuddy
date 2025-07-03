package presentation.spenddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun SpendDetailsScreen(
    component: SpendDetailsComponent,
    modifier: Modifier = Modifier,
) {
    val spendDetails by remember { mutableStateOf(component.spendDetails) }
    Column(modifier) {
        spendDetails.value?.let { (spend, splits, _) ->
            Text(spend.name)
            Text(spend.totalAmount.toString())
            Text("Added by ${component.userNames.value[spend.createdBy]} on 123")
//            repeat(splits.size) {
            val spenderOwes = component.calculateOwes(spend.spentBy)
            Text("${component.userNames.value[spend.spentBy]} paid ${spend.totalAmount}${if (spenderOwes != 0.0) " and owes $spenderOwes" else ""}")
//            }
            repeat(splits.size) {
                if (splits[it].userId != spend.spentBy)
                    Text(
                        "${component.userNames.value[splits[it].userId]} " +
                                "owes " +
                                "${
                                    component.calculateOwes(
                                        splits[it].userId
                                    )
                                }"
                    )
            }
        }
    }

}