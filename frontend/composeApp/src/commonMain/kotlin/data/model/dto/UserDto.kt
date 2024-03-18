package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
        val id: String,
        val email: String,
        val token: String
)