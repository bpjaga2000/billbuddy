package presentation.spenddetails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import constants.LentOrBorrowed

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
            val borrowers = splits.filter { it.lentOrBorrowed.toInt() == LentOrBorrowed.BORROWED }
            val lenders = splits.filter { it.lentOrBorrowed.toInt() == LentOrBorrowed.LENT }
            //TODO merge
            repeat(lenders.size) {
                Text("${component.userNames.value[lenders[it].userId]} paid ${lenders[it].value_}")//${if (spenderOwes != 0.0) " and owes $spenderOwes" else ""}")
            }
            repeat(borrowers.size) {
//                if (splits[it].userId != spend.spentBy)
                Text(
                    "${component.userNames.value[borrowers[it].userId]} " +
                            "owes " +
                            "${
                                component.calculateOwes(
                                    borrowers[it].userId
                                )
                            }"
                )
            }
        }
    }

}