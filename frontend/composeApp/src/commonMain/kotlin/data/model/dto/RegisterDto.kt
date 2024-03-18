package data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
        val email: String,
        val password: String
)