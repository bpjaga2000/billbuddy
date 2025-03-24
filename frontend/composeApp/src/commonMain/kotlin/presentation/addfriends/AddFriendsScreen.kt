package presentation.addfriends

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import presentation.common.GroupMemberItem
import presentation.common.TextEdit

@Composable
fun AddFriendsScreen(component: AddFriendsComponent, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        TextEdit(component.addFriendsString)
        LazyColumn {
            items(
                component.addFriendsResults.value.size,
                { it -> component.addFriendsResults.value[it].userId }) { it ->
                GroupMemberItem(component.addFriendsResults.value[it]) {
                    component.onAddFriendsResultTapped(it)
                }
            }
        }
        TextButton(onClick = { component.onDoneClicked() }) {
            Text("Done")
        }
    }

}