package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.GroupDto
import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.dto.UserIdListDto

interface GroupService {
    fun createGroup(groupDto: GroupDto): GroupResponseDto
    fun updateGroup(id: String, groupDto: GroupDto): GroupResponseDto
    fun deleteGroup(id: String): String
}