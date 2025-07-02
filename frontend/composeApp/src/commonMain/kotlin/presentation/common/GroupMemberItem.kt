package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.russhwolf.settings.get
import data.model.GroupMember
import utils.DataStore

@Composable
fun GroupMemberItem(member: GroupMember, onClick: (userId: String) -> Unit) {

    Row {
        Text(member.userId)
        if (member.userId != DataStore.settings.get<String>("id"))
            IconButton(onClick = { onClick(member.userId) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
            }
    }
}