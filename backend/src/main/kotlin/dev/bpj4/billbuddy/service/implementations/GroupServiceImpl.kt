package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.GroupDto
import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.entity.GroupMembersEntity
import dev.bpj4.billbuddy.mappings.mapToGroupEntity
import dev.bpj4.billbuddy.mappings.mapToGroupMembersDto
import dev.bpj4.billbuddy.mappings.mapToGroupResponseDto
import dev.bpj4.billbuddy.repository.GroupMemberRepository
import dev.bpj4.billbuddy.repository.GroupRepository
import dev.bpj4.billbuddy.service.GroupService
import org.springframework.stereotype.Service

@Service
class GroupServiceImpl(
        val groupRepository: GroupRepository,
        val groupMemberRepository: GroupMemberRepository
) : GroupService {

    override fun createGroup(groupDto: GroupDto): GroupResponseDto {
        val group = groupRepository.save(groupDto.mapToGroupEntity())
        val member = groupMemberRepository.save(GroupMembersEntity(group.groupOwnerId, group.id, group.groupOwnerId, group.groupOwnerId, null)).mapToGroupMembersDto()
        return group.mapToGroupResponseDto(listOf(member))
    }

    /*override fun getGroup(id: String): GroupResponseDto {
        if (groupRepository.existsById(id))

            return groupRepository.findById(id).get().mapToGroupResponseDto()
        else
            throw UsernameNotFoundException("Invalid User")
    }*/

    override fun updateGroup(id: String, groupDto: GroupDto): GroupResponseDto {
        if (groupRepository.existsById(id)) {
            return groupRepository.save(groupDto.mapToGroupEntity()).mapToGroupResponseDto(groupMemberRepository.findAllByGroupId(id).map { it.mapToGroupMembersDto() })
        } else
            throw Exception("Invalid Group")
    }

    override fun deleteGroup(id: String): String {
        return if (groupRepository.existsById(id)) {
            groupRepository.save(
                    groupRepository.findById(id).get().apply {
                        deletedAt = System.currentTimeMillis() / 1000
                        deletedAtFrontend = System.currentTimeMillis() / 1000
                    }
            )
            "deleted successfully"
        } else
            throw Exception("Invalid Group")
    }

}