package utils

import data.model.dto.GroupSettlesDto
import data.model.dto.SpendDto
import data.model.dto.SpendSplitDto
import dev.bpj4.billbuddy.tableandmigrations.GroupSettles
import dev.bpj4.billbuddy.tableandmigrations.SpendSplits
import dev.bpj4.billbuddy.tableandmigrations.Spends

fun Spends.mapToSpendDto() = SpendDto(
    id,
    name,
    totalAmount,
    isPayback,
    tag,
    groupId,
    spentAt,
    spentBy,
    createdBy,
    updatedBy,
    deletedBy,
    createdAt,
    updatedAt,
    deletedAt,
    createdAt,
    updatedAt,
    deletedAt
)

fun SpendSplits.mapToSpendSplitDto() = SpendSplitDto(
    id,
    userId,
    spendId,
    splitType.toInt(),
    value_,
    createdBy,
    updatedBy,
    deletedBy,
    createdAt,
    updatedAt,
    deletedAt,
    createdAt,
    updatedAt,
    deletedAt
)

fun GroupSettles.mapToGroupSettlesDto() = GroupSettlesDto(
    id,
    groupId,
    settledAt,
    createdAt,
    updatedAt,
    deletedAt,
    createdAt,
    updatedAt,
    deletedAt
)