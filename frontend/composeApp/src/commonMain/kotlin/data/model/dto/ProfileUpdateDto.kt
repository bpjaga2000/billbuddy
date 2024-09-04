package data.model.dto

data class ProfileUpdateDto(
        val id: String,
        val name: String,
        val mobileCountryCode: String?,
        val phone: Long?,
        val updatedAtFrontend: Long,
)