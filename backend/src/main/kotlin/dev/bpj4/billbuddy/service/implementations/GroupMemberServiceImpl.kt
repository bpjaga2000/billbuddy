package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.dto.UserIdListDto
import dev.bpj4.billbuddy.entity.GroupMembersEntity
import dev.bpj4.billbuddy.mappings.mapToGroupMembersDto
import dev.bpj4.billbuddy.mappings.mapToGroupResponseDto
import dev.bpj4.billbuddy.repository.GroupMemberRepository
import dev.bpj4.billbuddy.repository.GroupRepository
import dev.bpj4.billbuddy.service.GroupMemberService
import org.springframework.stereotype.Service

@Service
class GroupMemberServiceImpl(
        val groupRepository: GroupRepository,
        val groupMemberRepository: GroupMemberRepository
) : GroupMemberService {
    override fun addGroupMembers(id: String, userIds: UserIdListDto): GroupResponseDto {
        if (groupRepository.existsById(id)) {
            val newMembers = userIds.ids
                    .map {
                        GroupMembersEntity(it, id, userIds.requesterId, userIds.requesterId, null)
                    }
            groupMemberRepository.saveAll(newMembers)
            return groupRepository.save(groupRepository.findById(id).get()).mapToGroupResponseDto(groupMemberRepository.findAllByGroupId(id).map { it.mapToGroupMembersDto() })
        } else
            throw Exception("Invalid Group")
    }

    override fun removeGroupMembers(id: String, userIds: UserIdListDto): GroupResponseDto {
        if (groupRepository.existsById(id)) {
            val existingMembers = groupMemberRepository.findAllByGroupId(id)
            val removedMembers = existingMembers
                    .onEach {
                        it.deletedBy = userIds.requesterId
                        it.deletedAt = System.currentTimeMillis() / 1000
                        it.updatedBy = userIds.requesterId
                        it.updatedAt = System.currentTimeMillis() / 1000
                        it.deletedAtFrontend = System.currentTimeMillis() / 1000
                    }
            groupMemberRepository.saveAll(removedMembers)
            return groupRepository.save(groupRepository.findById(id).get()).mapToGroupResponseDto(groupMemberRepository.findAllByGroupId(id).map { it.mapToGroupMembersDto() })
        } else
            throw Exception("Invalid Group")
    }

}