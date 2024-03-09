package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.dto.UserIdListDto
import dev.bpj4.billbuddy.service.GroupMemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/group/manage")
class GroupMemberController(
        val groupMemberService: GroupMemberService
) {

    @PostMapping("add/{id}")
    fun addMembersToGroup(@PathVariable id: String, @RequestBody userIds: UserIdListDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity(groupMemberService.addGroupMembers(id, userIds), HttpStatus.ACCEPTED)
    }

    @PostMapping("remove/{id}")
    fun removeMembersFromGroup(@PathVariable id: String, @RequestBody userIds: UserIdListDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity(groupMemberService.removeGroupMembers(id, userIds), HttpStatus.ACCEPTED)
    }

}