package dev.bpj4.billbuddy.dto

import dev.bpj4.billbuddy.entity.GroupTags

data class GroupResponseDto(
        val id: String,
        val name: String,
        val tag: GroupTags,
        val ownerId: String,
        val members: List<GroupMembersDto>
)