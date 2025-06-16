package presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.model.Balance
import dev.bpj4.billbuddy.tableandmigrations.Groups
import presentation.common.FriendListItem
import presentation.common.GroupListItem

@Composable
fun HomeScreen(component: HomeComponent, modifier: Modifier = Modifier) {
    val groups = remember { mutableStateListOf<Groups>() }
    val friendBalances = remember { mutableStateListOf<Balance>() }

    component.groups.subscribe {
        groups.clear()
        groups.addAll(it)
    }
    component.friendBalances.subscribe {
        friendBalances.clear()
        friendBalances.addAll(it)
    }
    Column(
        modifier = modifier.then(Modifier.fillMaxSize().padding(horizontal = 16.dp)),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Balances")
        LazyHorizontalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.height(120.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            repeat(friendBalances.size) {
                item {
                    FriendListItem(friendBalances[it])
                }
            }
        }
        Text("Groups")
        LazyColumn {
            repeat(groups.size) {
                item {
                    GroupListItem({ component.onGroupClick(null) })
                }
            }
        }
    }
}