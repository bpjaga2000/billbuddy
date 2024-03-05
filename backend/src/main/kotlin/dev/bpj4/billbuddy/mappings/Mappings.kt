package dev.bpj4.billbuddy.mappings

import dev.bpj4.billbuddy.dto.RegisterDto
import dev.bpj4.billbuddy.entity.UserEntity

fun RegisterDto.mapToUserEntity(): UserEntity = UserEntity(email = this.email, password = this.password)