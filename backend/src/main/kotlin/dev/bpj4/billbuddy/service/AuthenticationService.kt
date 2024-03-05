package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.LoginDto
import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.dto.UserDto

interface AuthenticationService {

    fun createUser(registerDto: RegisterDto): UserDto?
    fun verifyUser(loginDto: LoginDto): UserDto?
}