package data.model.dto

import data.GroupTags

data class GroupResponseDto(
    val id: String,
    val name: String,
    val tag: GroupTags,
    val ownerId: String,
    val members: List<GroupMembersDto>
)