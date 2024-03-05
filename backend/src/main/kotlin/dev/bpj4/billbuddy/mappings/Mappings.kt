package dev.bpj4.billbuddy.mappings

import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.entity.UserEntity

fun RegisterDto.mapToUserEntityForRegistration(encodedPassword: String): UserEntity = UserEntity(email = this.email, password = encodedPassword)