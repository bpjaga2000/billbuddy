package presentation.groupsettings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import presentation.common.GroupMemberItem
import presentation.common.TextEdit

@Composable
fun GroupSettingsScreen(component: GroupSettingsComponent, modifier: Modifier = Modifier) {

    val groupName = remember { mutableStateOf(component.groupName) }

    TextEdit(groupName, "Group Name")

    LazyColumn {
        items(component.groupMembers.size, key = { it -> component.groupMembers[it].userId }) {
            GroupMemberItem(
                component.groupMembers[it],
                component::onRemoveMemberClicked
            )
        }
    }

}