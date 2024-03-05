package dev.bpj4.billbuddy.service.implementations

import dev.bpj4.billbuddy.dto.LoginDto
import dev.bpj4.billbuddy.dto.LogoutDto
import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.dto.UserDto
import dev.bpj4.billbuddy.entity.TokenBlacklistEntity
import dev.bpj4.billbuddy.mappings.mapToUserEntityForRegistration
import dev.bpj4.billbuddy.repository.TokenBlacklistRepository
import dev.bpj4.billbuddy.repository.UserRepository
import dev.bpj4.billbuddy.security.JwtGenerator
import dev.bpj4.billbuddy.service.AuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticationServiceImpl(
        val userRepository: UserRepository,
        val tokenBlacklistRepository: TokenBlacklistRepository,
        val jwtGenerator: JwtGenerator,
        var authManager: AuthenticationManager,
        val passwordEncoder: BCryptPasswordEncoder
) : AuthenticationService {

    @Transactional
    override fun createUser(registerDto: RegisterDto): UserDto? {
        return if (userRepository.existsByEmail(registerDto.email))
            null
        else
            userRepository.save(registerDto.mapToUserEntityForRegistration(passwordEncoder.encode(registerDto.password))).run {
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

    @Transactional
    override fun isTokenBlackListed(token: String): Boolean {
        return tokenBlacklistRepository.existsByToken(token)
    }

    @Transactional
    override fun logoutUser(logoutDto: LogoutDto): String? {
        return if (userRepository.existsByEmail(logoutDto.email)) {
            tokenBlacklistRepository.save(TokenBlacklistEntity(token = logoutDto.token))
            "saved"
        } else
            null
    }

}