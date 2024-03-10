package dev.bpj4.billbuddy.controller

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.dto.ProfileUpdateDto
import dev.bpj4.billbuddy.service.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/profile")
class ProfileController(
        private val profileService: ProfileService
) {

    @GetMapping("{id}")
    fun getProfile(@PathVariable("id") id: String): ResponseEntity<ProfileDto> = ResponseEntity(profileService.getProfile(id), HttpStatus.OK)

    @PostMapping("{id}")
    fun updateProfile(@PathVariable("id") id: String, @RequestBody profileUpdateDto: ProfileUpdateDto): ResponseEntity<ProfileDto> = ResponseEntity(profileService.updateProfile(profileUpdateDto), HttpStatus.ACCEPTED)

    @DeleteMapping("{id}")
    fun deleteProfile(@PathVariable("id") id: String): ResponseEntity<String> = ResponseEntity(profileService.deleteProfile(id), HttpStatus.NO_CONTENT)

    @DeleteMapping("perm/{id}")
    fun permanentlyDeleteProfile(@PathVariable("id") id: String): ResponseEntity<String> = ResponseEntity(profileService.permanentlyDeleteProfile(id), HttpStatus.NO_CONTENT)

}