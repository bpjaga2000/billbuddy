package data.model.dto

import data.SpendTags
import kotlinx.serialization.Serializable

@Serializable
data class SpendDto(
    val id: String,
    val name: String,
    val totalAmount: Double,
    val isPayback: Boolean,
    val tag: SpendTags,
    val groupId: String,
    val spentAt: Long,
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
