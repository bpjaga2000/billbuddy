package dev.bpj4.billbuddy.dto

import dev.bpj4.billbuddy.entity.GroupTags

data class GroupSyncResponseDto(
        val id: String,
        val name: String,
        val tag: GroupTags,
        val ownerId: String,
        val createdAtFrontend: Long,
        val updatedAtFrontend: Long,
        val deletedAtFrontend: Long?,
        val createdAt: Long,
        val updatedAt: Long,
        val deletedAt: Long?
)