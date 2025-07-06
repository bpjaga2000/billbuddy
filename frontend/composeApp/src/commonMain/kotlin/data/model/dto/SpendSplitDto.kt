package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpendSplitDto(
        val id: String,
        val userId: String,
        val spendId: String,
        val splitType: Int,
        val value: Double,
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
