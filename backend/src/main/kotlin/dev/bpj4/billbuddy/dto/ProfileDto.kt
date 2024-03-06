package dev.bpj4.billbuddy.dto

data class ProfileDto(
        val id: String,
        val name: String,
        val email: String,
        val mobileCountryCode: String?,
        val phone: Long?,
        val createdAtFrontend: Long,
        val updatedAtFrontend: Long,
        val deletedAtFrontend: Long?,
        val createdAt: Long,
        val updatedAt: Long,
        val deletedAt: Long?
)