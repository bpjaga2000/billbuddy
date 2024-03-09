package dev.bpj4.billbuddy.dto

import dev.bpj4.billbuddy.entity.SpendTags

data class SpendDto(
        val id: String,
        val totalAmount: Float,
        val isPayback: Boolean,
        val tags: SpendTags,
        val groupId: String,
        val createdBy: String,
        val updatedBy: String,
        val deletedBy: String?,
        val createdAtFrontend: Long,
        val updatedAtFrontend: Long,
        val deletedAtFrontend: Long?,
        val createdAt: Long,
        val updatedAt: Long,
        val deletedAt: Long?
)
