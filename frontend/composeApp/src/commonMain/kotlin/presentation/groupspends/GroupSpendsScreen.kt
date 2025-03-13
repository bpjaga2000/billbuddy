package presentation.groupspends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.common.SpendListItem

@Composable
fun GroupSpendsScreen(component: GroupSpendsComponent, modifier: Modifier = Modifier) {

    Box {
        Column(modifier = modifier.then(Modifier.padding(horizontal = 16.dp))) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Text("Settle Up")
                }
                Button(onClick = { /*TODO*/ }) {
                    Text("Balances")
                }
            }

            LazyColumn {
                repeat(10) {
                    item {
                        SpendListItem({ component.onGroupSpendClicked() })
                    }
                }
            }
        }
        IconButton(
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp).size(48.dp),
            onClick = component::onAddSpendClicked
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Add Expense"
            )
        }
    }
}