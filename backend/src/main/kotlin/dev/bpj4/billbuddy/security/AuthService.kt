package dev.bpj4.billbuddy.security

import dev.bpj4.billbuddy.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
        private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByEmail(username).orElseThrow { throw UsernameNotFoundException("User with username $username not found") }
        return User(user.email, user.password, mutableListOf(SimpleGrantedAuthority(user.role)))
    }
}