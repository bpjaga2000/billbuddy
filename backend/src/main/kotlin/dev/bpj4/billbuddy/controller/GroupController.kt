package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.GroupDto
import dev.bpj4.billbuddy.dto.GroupResponseDto
import dev.bpj4.billbuddy.dto.UserIdListDto
import dev.bpj4.billbuddy.service.GroupService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/group")
class GroupController(
        val groupService: GroupService
) {

    @PostMapping
    fun createGroup(@RequestBody groupDto: GroupDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity(groupService.createGroup(groupDto), HttpStatus.CREATED)
    }

    @PutMapping("{id}")
    fun updateGroup(@PathVariable id: String, @RequestBody groupDto: GroupDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity(groupService.updateGroup(id, groupDto), HttpStatus.ACCEPTED)
    }

    @DeleteMapping("{id}")
    fun deleteGroup(@PathVariable id: String, @RequestBody groupDto: GroupDto): ResponseEntity<String> {
        return ResponseEntity(groupService.deleteGroup(id, groupDto), HttpStatus.OK)
    }

}