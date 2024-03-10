package dev.bpj4.billbuddy.dto

data class UpSyncDto(
        val spends: List<SpendDto>,
        val spendSplit: List<SpendSplitDto>
)