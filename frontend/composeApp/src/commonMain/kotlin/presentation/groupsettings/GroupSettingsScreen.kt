package presentation.groupsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import presentation.common.GroupMemberItem
import presentation.common.TextEdit

@Composable
fun GroupSettingsScreen(component: GroupSettingsComponent, modifier: Modifier = Modifier) {

    val groupName = remember { mutableStateOf(component.groupName) }

    Column(modifier = modifier.then(Modifier)) {

        TextEdit(groupName, "Group Name")

        LazyColumn {
            items(component.groupMembers.size, key = { it -> component.groupMembers[it].userId }) {
                GroupMemberItem(
                    component.groupMembers[it],
                    component::onRemoveMemberClicked
                )
            }
        }

        TextButton(onClick = { component.onAddMemberClicked() }) { Text("Add members") }
    }

}