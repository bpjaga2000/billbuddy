package data.model

import dev.bpj4.billbuddy.tableandmigrations.GroupMembers

data class GroupMemberSplit(
    val userId: String,
    val userName: String,
    val isInvolved: Boolean,
    var splitValue: Float = 0f,
)

fun GroupMembers.toGroupMemberSplit(name: String): GroupMemberSplit {
    return GroupMemberSplit(
        userId,
        name,
        false
    )
}