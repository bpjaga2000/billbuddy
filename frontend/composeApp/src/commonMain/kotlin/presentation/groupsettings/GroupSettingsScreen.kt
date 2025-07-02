package presentation.groupsettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import presentation.common.GroupMemberItem
import presentation.common.TextEdit

@Composable
fun GroupSettingsScreen(component: GroupSettingsComponent, modifier: Modifier = Modifier) {

    val groupName = remember { mutableStateOf(component.groupName) }
    val groupMembers by remember { mutableStateOf(component.groupMembers) }

    Column(modifier = modifier.then(Modifier), horizontalAlignment = Alignment.CenterHorizontally) {

        TextEdit(groupName, "Group Name")

        LazyColumn {
            items(groupMembers.value.size, key = { it -> groupMembers.value[it].userId }) {
                GroupMemberItem(
                    groupMembers.value[it],
                    component::onRemoveMemberClicked
                )
            }
        }

        TextButton(
            onClick = {
                component.groupName = groupName.value
                component.onAddMemberClicked() }) { Text("Add members") }
    }

}