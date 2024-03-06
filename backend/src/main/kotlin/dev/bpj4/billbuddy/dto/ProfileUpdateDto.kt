package dev.bpj4.billbuddy.dto

data class ProfileUpdateDto(
        val id: String,
        val name: String,
        val mobileCountryCode: String?,
        val phone: Long?,
        val updatedAtFrontend: Long,
)