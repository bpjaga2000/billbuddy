package presentation.addfriends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import presentation.common.SearchMemberItem
import presentation.common.TextEdit

@Composable
fun AddFriendsScreen(component: AddFriendsComponent, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.then(Modifier.fillMaxSize()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextEdit(
            text = component.searchTag,
            onValueChange = { component.onAddFriendsClicked(it) })
        LazyColumn {
            items(
                component.addFriendsResults.value.size,
                { it -> component.addFriendsResults.value[it].id }) { it ->
                SearchMemberItem(component.addFriendsResults.value[it]) { id, isChecked ->
                    component.onAddFriendsResultTapped(id, isChecked)
                }
            }
        }
        TextButton(onClick = { component.onDoneClicked() }) {
            Text("Done")
        }
    }

}