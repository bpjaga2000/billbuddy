package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.ProfileDto
import dev.bpj4.billbuddy.dto.ProfileUpdateDto
import dev.bpj4.billbuddy.mappings.mapToProfileDto
import dev.bpj4.billbuddy.mappings.mapToUserEntity
import dev.bpj4.billbuddy.repository.UserRepository
import dev.bpj4.billbuddy.service.ProfileService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class ProfileServiceImpl(
        private val userRepository: UserRepository
) : ProfileService {

    override fun getProfile(id: String): ProfileDto {
        if (userRepository.existsById(id))
            return userRepository.findById(id).get().mapToProfileDto()
        else
            throw UsernameNotFoundException("Invalid User")
    }

    override fun updateProfile(profileUpdateDto: ProfileUpdateDto): ProfileDto {
        if (userRepository.existsById(profileUpdateDto.id))
            return userRepository.save(
                    profileUpdateDto.mapToUserEntity(
                            userRepository.findById(profileUpdateDto.id).get()
                    )
            ).mapToProfileDto()
        else
            throw UsernameNotFoundException("Invalid User")
    }

    override fun deleteProfile(id: String): String {
        return if (userRepository.existsById(id)) {
            userRepository.save(
                    userRepository.findById(id).get().apply {
                        deletedAt = System.currentTimeMillis() / 1000
                        deletedAtFrontend = System.currentTimeMillis() / 1000
                    }
            )
            "deleted successfully"
        } else
            throw UsernameNotFoundException("Invalid User")
    }

    override fun permanentlyDeleteProfile(id: String): String {
        return if (userRepository.existsById(id)) {
            userRepository.delete(userRepository.findById(id).get())
            "deleted successfully"
        } else
            throw UsernameNotFoundException("Invalid User")
    }

}