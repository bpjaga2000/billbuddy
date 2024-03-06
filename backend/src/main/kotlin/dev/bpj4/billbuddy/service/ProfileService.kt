package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.dto.ProfileUpdateDto

interface ProfileService {
    fun getProfile(id: String): ProfileDto
    fun updateProfile(profileUpdateDto: ProfileUpdateDto): ProfileDto
    fun deleteProfile(id: String): String
    fun permanentlyDeleteProfile(id: String): String
}