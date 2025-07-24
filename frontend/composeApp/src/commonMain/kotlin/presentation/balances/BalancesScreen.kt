package presentation.balances

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import presentation.common.BalanceItem

@Composable
fun BalancesScreen(component: BalancesComponent, modifier: Modifier = Modifier) {

    val balances by remember { component.balances }

    LazyColumn(modifier = modifier) {
        val keys = balances.keys.toList()
        items(keys.size, { it -> keys[it] }) {
            BalanceItem(
                balances[keys[it]]!!,
                balances[keys[it]]!!.userSettles!!.values.toList(),
                { groupId, payerId, payeeId ->
                    component.onSettleUpClicked(groupId, payerId, payeeId)
                },
                component.getCurrentUserId()
            )
        }
    }

}