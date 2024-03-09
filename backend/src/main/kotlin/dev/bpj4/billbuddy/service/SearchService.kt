package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.ProfileDto

interface SearchService {

    fun search(query: String): List<ProfileDto>
}