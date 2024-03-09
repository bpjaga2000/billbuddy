package dev.bpj4.billbuddy.dto

import dev.bpj4.billbuddy.entity.GroupTags

data class GroupDto(
        val name:String,
        val tag: GroupTags,
        val ownerId: String
)