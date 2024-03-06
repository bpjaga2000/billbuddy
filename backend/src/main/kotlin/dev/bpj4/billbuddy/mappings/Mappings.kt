package dev.bpj4.billbuddy.mappings

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.dto.ProfileUpdateDto
import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.entity.UserEntity

fun RegisterDto.mapToUserEntityForRegistration(encodedPassword: String): UserEntity = UserEntity(email = this.email, password = encodedPassword)

fun UserEntity.mapToProfileDto() = ProfileDto(
        id,
        name,
        email,
        mobileCountryCode,
        phone,
        createdAtFrontend,
        updatedAtFrontend,
        deletedAtFrontend,
        createdAt,
        updatedAt,
        deletedAt
)

fun ProfileUpdateDto.mapToUserEntity(existingUserEntity: UserEntity) = UserEntity(
        name = name,
        mobileCountryCode = mobileCountryCode,
        phone = phone,
).apply {
    id = this@mapToUserEntity.id
    createdAt = existingUserEntity.createdAt
    deletedAt = existingUserEntity.deletedAt
    createdAtFrontend = existingUserEntity.createdAtFrontend
    updatedAtFrontend = this@mapToUserEntity.updatedAtFrontend
    deletedAtFrontend = existingUserEntity.deletedAtFrontend
}