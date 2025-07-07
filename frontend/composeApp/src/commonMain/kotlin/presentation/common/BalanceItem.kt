@file:OptIn(ExperimentalFoundationApi::class)

package presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.Balance
import utils.DataStore
import kotlin.math.absoluteValue

@Composable
fun BalanceItem(
    payer: Balance,
    payees: List<Balance>,
    onClick: (String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    var isCollapsed by mutableStateOf(false)

    Column(
        modifier.then(
            Modifier.clickable {
                isCollapsed = !isCollapsed
            }
        )
    ) {

        Row(
            Modifier.height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = if (isCollapsed) Icons.Default.KeyboardArrowRight else Icons.Default.KeyboardArrowDown,
                contentDescription = null
            )

            Text(
                "${payer.name} " + (if (payer.amount > 0.0) "gets" else "owes") + " ₹${payer.amount.absoluteValue}"
            )

        }

        AnimatedVisibility(!isCollapsed) {
            LazyColumn(Modifier.height((20 * payees.size).dp)) {
                items(payees.size) { index ->
                    Text(
                        modifier = Modifier.height(20.dp),
                        text = "${payees[index].name} " + (if (payees[index].amount > 0.0) "gets" else "owes") + " ₹${payees[index].amount.absoluteValue}"
                    )
                    if (payer.userId == DataStore.settings.getStringOrNull("id") && payees[index].amount != 0.0)
                        Button(
                            onClick = {
                                onClick(
                                    payer.groupId!!,
                                    payer.userId,
                                    payees[index].userId
                                )
                            },
                            modifier = Modifier.height(20.dp)
                        ) {
                            Text("Settle Up")
                        }
                }
            }
        }
    }

}