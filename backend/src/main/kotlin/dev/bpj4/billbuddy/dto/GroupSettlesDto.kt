package dev.bpj4.billbuddy.dto

data class GroupSettlesDto(
        val id: String,
        val groupId: String,
        val settledAt: Long,
        val createdAtFrontend: Long,
        val updatedAtFrontend: Long,
        val deletedAtFrontend: Long?,
        val createdAt: Long,
        val updatedAt: Long,
        val deletedAt: Long?
)
