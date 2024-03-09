package dev.bpj4.billbuddy.dto

data class GroupMembersDto(
        val id: String,
        val userId: String,
        val groupId: String,
        val createdAt: Long,
        val createdBy: String,
        val updatedAt: Long,
        val updatedBy: String,
        val deletedAt: Long?,
        val deletedBy: String?
)