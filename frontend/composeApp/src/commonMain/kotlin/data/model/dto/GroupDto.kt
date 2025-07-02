package data.model.dto

import data.GroupTags
import kotlinx.serialization.Serializable

@Serializable
data class GroupDto(
    val name: String,
    val tag: GroupTags,
    val userId: String
)