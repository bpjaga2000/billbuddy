package dev.bpj4.billbuddy.dto

data class UserIdListDto(
        val ids: List<String> = listOf(),
        val requesterId: String
)