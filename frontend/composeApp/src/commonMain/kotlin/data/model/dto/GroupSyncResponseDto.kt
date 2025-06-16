package data.model.dto

import data.GroupTags
import kotlinx.serialization.Serializable

@Serializable
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