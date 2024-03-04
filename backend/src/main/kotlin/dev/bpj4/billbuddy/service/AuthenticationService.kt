package dev.bpj4.billbuddy.service

import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.entity.UserEntity

interface AuthenticationService {

    fun createUser(registerDto: RegisterDto): UserEntity?
}