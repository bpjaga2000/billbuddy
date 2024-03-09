package dev.bpj4.billbuddy.dto

data class SpendSplitDto(
        val id: String,
        val userId: String,
        val spendId: String,
        val lentOrBorrowed: Int,
        val splitType: Int,
        val value: Float,
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
