package presentation.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import data.model.GroupMember

@Composable
fun GroupMemberItem(member: GroupMember, onClick: (userId: String) -> Unit) {

    Row {
        Text(member.userName)
        IconButton(onClick = { onClick(member.userId) }) {
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
        }
    }
}