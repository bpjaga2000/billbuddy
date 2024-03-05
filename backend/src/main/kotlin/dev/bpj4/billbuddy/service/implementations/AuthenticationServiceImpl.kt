package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.LoginDto
import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.dto.UserDto
import dev.bpj4.billbuddy.mappings.mapToUserEntity
import dev.bpj4.billbuddy.repository.UserRepository
import dev.bpj4.billbuddy.security.JwtGenerator
import dev.bpj4.billbuddy.service.AuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
        val userRepository: UserRepository,
        val jwtGenerator: JwtGenerator,
        var authManager: AuthenticationManager
) : AuthenticationService {

    override fun createUser(registerDto: RegisterDto): UserDto? {
        return if (userRepository.existsByEmail(registerDto.email))
            null
        else
            userRepository.save(registerDto.mapToUserEntity()).run {
                UserDto(id, email, jwtGenerator.generateJwToken(
                        UsernamePasswordAuthenticationToken(email, password, mutableListOf(SimpleGrantedAuthority(role)))
                ))
            }
    }

    override fun verifyUser(loginDto: LoginDto): UserDto? {
        return if (userRepository.existsByEmail(loginDto.email)) {
            val authentication = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)
            SecurityContextHolder.getContext().authentication = authManager.authenticate(authentication)
            userRepository.findByEmail(loginDto.email).get().run {
                UserDto(id, email, jwtGenerator.generateJwToken(authentication))
            }
        } else
            null
    }

}