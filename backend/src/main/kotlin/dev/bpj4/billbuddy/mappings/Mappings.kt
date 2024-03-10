package dev.bpj4.billbuddy.mappings

import dev.bpj4.billbuddy.dto.*
import dev.bpj4.billbuddy.entity.*

fun RegisterDto.mapToUserEntityForRegistration(encodedPassword: String): UserEntity =
        UserEntity(email = this.email, password = encodedPassword)

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

fun GroupDto.mapToGroupEntity(updatedBy: String = userId) = GroupEntity(
        name, tag, updatedBy = updatedBy
)

fun GroupDto.mapToGroupEntity(groupEntity: GroupEntity) = GroupEntity(
        name, tag, userId, groupEntity.createdBy, groupEntity.updatedBy, groupEntity.deletedBy
).apply {
    id = groupEntity.id
    createdAt = groupEntity.createdAt
    deletedAt = groupEntity.deletedAt
    createdAtFrontend = groupEntity.createdAtFrontend
    updatedAtFrontend = groupEntity.updatedAtFrontend
    deletedAtFrontend = groupEntity.deletedAtFrontend
}

fun GroupEntity.mapToGroupDto() = GroupDto(
        name, tag, groupOwnerId
)

fun GroupEntity.mapToGroupResponseDto(list: List<GroupMembersDto>) = GroupResponseDto(
        id, name, tag, groupOwnerId, list
)

fun GroupEntity.mapToGroupSyncResponseDto() = GroupSyncResponseDto(
        id, name, tag, groupOwnerId, createdAtFrontend, updatedAtFrontend, deletedAtFrontend, createdAt, updatedAt, deletedAt
)

fun GroupMembersEntity.mapToGroupMembersDto() = GroupMembersDto(
        id, userId, groupId, createdAt, createdBy, updatedAt, updatedBy, deletedAt, deletedBy
)

fun SpendEntity.mapToSpendDto() = SpendDto(
        id,
        totalAmount,
        isPayback,
        tag,
        groupId,
        createdBy,
        updatedBy,
        deletedBy,
        createdAtFrontend,
        updatedAtFrontend,
        deletedAtFrontend,
        createdAt,
        updatedAt,
        deletedAt
)

fun SpendDto.mapToSpendEntity() = SpendEntity(
        totalAmount,
        isPayback,
        tag,
        groupId,
        createdBy,
        updatedBy,
        deletedBy
).also {
    it.id = id
    it.createdAtFrontend = createdAtFrontend
    it.updatedAtFrontend = updatedAtFrontend
    it.deletedAtFrontend = deletedAtFrontend
    it.createdAt = createdAt
    it.updatedAt = updatedAt
    it.deletedAt = deletedAt
}

fun SpendSplitEntity.mapToSpendSplitDto() = SpendSplitDto(
        id,
        userId,
        spendId,
        lentOrBorrowed,
        splitType,
        value,
        createdBy,
        updatedBy,
        deletedBy,
        createdAtFrontend,
        updatedAtFrontend,
        deletedAtFrontend,
        createdAt,
        updatedAt,
        deletedAt
)

fun SpendSplitDto.mapToSpendSplitEntity() = SpendSplitEntity(
        userId,
        spendId,
        lentOrBorrowed,
        splitType,
        value,
        createdBy,
        updatedBy,
        deletedBy
).also {
    it.id = id
    it.createdBy = createdBy
    it.updatedBy = updatedBy
    it.deletedBy = deletedBy
    it.createdAtFrontend = createdAtFrontend
    it.updatedAtFrontend = updatedAtFrontend
    it.deletedAtFrontend = deletedAtFrontend
    it.createdAt = createdAt
    it.updatedAt = updatedAt
    it.deletedAt = deletedAt
}