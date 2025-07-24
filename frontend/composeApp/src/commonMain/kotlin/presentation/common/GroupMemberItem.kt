package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import data.model.GroupMember

@Composable
fun GroupMemberItem(member: GroupMember, onClick: (userId: String) -> Unit, currentUserId: String) {

    Row {
        Text(member.userId)
        if (member.userId != currentUserId)
            IconButton(onClick = { onClick(member.userId) }) {
                Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
            }
    }
}