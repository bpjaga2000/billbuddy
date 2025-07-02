package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserIdListDto(
    val ids: List<String> = listOf(),
    val requesterId: String
)