package presentation.balances

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.common.BalanceItem

@Composable
fun BalancesScreen(component: BalancesComponent, modifier: Modifier = Modifier) {

    LazyColumn(modifier = modifier) {
        items(component.lentBalances.size, { it -> component.lentBalances[it].userId }) {
            BalanceItem(component.lentBalances[it], component.borrowedBalances[it])
        }
    }

}