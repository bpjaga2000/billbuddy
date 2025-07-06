package presentation.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.common.FriendListItem
import presentation.common.GroupListItem

@Composable
fun HomeScreen(component: HomeComponent, modifier: Modifier = Modifier) {
    val groups by remember { component.groups }
    val friendBalances by remember { component.friendBalances }

    Box {
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
                items(friendBalances.size) {
                    FriendListItem(friendBalances[it], {component.onFriendBalanceClicked(friendBalances[it].userId)})
                }
            }
            Text("Groups")
            LazyColumn {
                repeat(groups.size) {
                    item {
                        groups[it].let { group ->
                            GroupListItem(group, { component.onGroupClick(group.group.id) })
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            { component.onCreateGroupClicked() },
            modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp)
        ) {
            Text("+", fontSize = 32.sp)
        }
    }
}