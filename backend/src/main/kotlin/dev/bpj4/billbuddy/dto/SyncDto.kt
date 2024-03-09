package dev.bpj4.billbuddy.dto

data class SyncDto(
        val users: List<ProfileDto>,
        val groups: List<GroupSyncResponseDto>,
        val groupMembers: List<GroupMembersDto>,
        val spends: List<SpendDto>,
        val spendSplit: List<SpendSplitDto>
)