package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.mappings.mapToProfileDto
import dev.bpj4.billbuddy.repository.UserRepository
import dev.bpj4.billbuddy.service.SearchService
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(
        val userRepository: UserRepository
) : SearchService {

    override fun search(query: String): List<ProfileDto> {//todo fuzzy logic
        return userRepository.search(query).map { it.mapToProfileDto() }
    }

}