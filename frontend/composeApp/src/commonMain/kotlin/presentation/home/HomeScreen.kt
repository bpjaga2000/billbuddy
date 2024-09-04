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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import presentation.common.FriendListItem
import presentation.common.GroupListItem

@Composable
fun HomeScreen(component: HomeComponent, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.then(Modifier.fillMaxSize().padding(horizontal = 16.dp)),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Friends")
        LazyHorizontalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.height(120.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for (i in 1..10) {
                item {
                    FriendListItem()
                }
            }
        }
        Text("Groups")
        LazyColumn {
            for (i in 1..10) {
                item {
                    GroupListItem({ component.onGroupClick(null) })
                }
            }
        }
    }
}