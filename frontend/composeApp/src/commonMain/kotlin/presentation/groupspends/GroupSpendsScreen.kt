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
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.get
import data.model.SpendWithSplit
import presentation.common.SpendListItem
import utils.DataStore

@Composable
fun GroupSpendsScreen(component: GroupSpendsComponent, modifier: Modifier = Modifier) {

    var spends by remember { mutableStateOf(listOf<SpendWithSplit>()) }
    component.spendList.subscribe {
        spends = it
    }

    Box(modifier = modifier.then(Modifier.fillMaxSize())) {
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
                repeat(spends.size) {
                    item(spends[it].spend.id) {
                        SpendListItem(
                            spends[it],
                            { component.onGroupSpendClicked(spends[it].spend.id) },
                            component.getUserNameFromId(spends[it].spend.spentBy),
                            spends[it].splits.find { it -> it.id == DataStore.settings.get<String>("id") } != null
                        )
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