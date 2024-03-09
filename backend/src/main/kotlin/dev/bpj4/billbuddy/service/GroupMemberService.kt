package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.dto.UserIdListDto

interface GroupMemberService {
    fun addGroupMembers(id: String, userIds: UserIdListDto): GroupResponseDto
    fun removeGroupMembers(id: String, userIds: UserIdListDto): GroupResponseDto
}