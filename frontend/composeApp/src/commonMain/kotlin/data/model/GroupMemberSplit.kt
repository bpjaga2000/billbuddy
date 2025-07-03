package data.model

import dev.bpj4.billbuddy.tableandmigrations.GroupMembers

data class GroupMemberSplit(
    val userId: String,
    val userName: String,
    val isInvolved: Boolean,
    var splitValue: Double = 0.0,
)

fun GroupMembers.toGroupMemberSplit(name: String): GroupMemberSplit {
    return GroupMemberSplit(
        userId,
        name,
        false
    )
}